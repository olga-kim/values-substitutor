package logic;

import model.ExecutionParams;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class ValuesReplacer {

    private static Properties readProperties(File fileName) {
        Properties props = new Properties();

        try (FileReader reader = new FileReader(fileName)) {
            props.load(reader);
        } catch (FileNotFoundException e) {
            System.out.println("Properties file was not found");
        } catch (IOException e) {
            System.out.println("I/O error was found");
        }

        return props;
    }

    private static void replace(Element element, Properties props) {
        if (!element.elements().isEmpty()) {
            Iterator<Element> innerIterator = element.elementIterator();
            while (innerIterator.hasNext()) {
                replace(innerIterator.next(), props);
            }
        } else {
            String extractedValue = props.getProperty("\"" + element.getText()
                    .replaceAll("\\{|\\}|\\$", "") + "\"")
                    .replaceAll("\"", "");
            extractedValue = extractedValue.contains("<") ? generateValue(extractedValue) : extractedValue;
            element.setText(extractedValue);
        }
    }

    private static String generateValue(String property) {
        if (property.toUpperCase().contains("ID")) {
            return String.valueOf(UUID.randomUUID());
        }
        if (property.toUpperCase().contains("DATETIME")) {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        } else return RandomStringUtils.randomAlphabetic(10);
    }

    private static File getCheckedFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException(filePath + " is not a valid file path");
        }
        return file;
    }

    public static void process(Map<ExecutionParams, String> parsedParams) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(getCheckedFile(parsedParams.get(ExecutionParams.INOUT)));
        Element description = document.getRootElement();
        replace(description, readProperties(getCheckedFile(parsedParams.get(ExecutionParams.PROPERTIES))));
        writeResult(parsedParams.get(ExecutionParams.OUTPUT), description.asXML());
    }

    private static void writeResult(String outputFilePath, String result) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error was found while writing result");
        }
    }
}
