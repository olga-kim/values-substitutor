package logic;

import model.ExecutionParams;
import model.GeneratedValues;
import model.Messages;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValuesReplacer {

    private static Logger log = Logger.getLogger(ValuesReplacer.class.getName());

    private static Properties readProperties(File fileName) {
        Properties props = new Properties();

        try (FileReader reader = new FileReader(fileName)) {
            props.load(reader);
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, String.format(Messages.FILE_NOT_FOUND.value, fileName));
        } catch (IOException e) {
            log.log(Level.SEVERE, String.format(Messages.IO_ERROR.value, "reading .properties file"));
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
            try {
                String extractedValue = props.getProperty("\"" + element.getText()
                        .replaceAll("\\{|\\}|\\$", "") + "\"")
                        .replaceAll("\"", "");
                extractedValue = extractedValue.startsWith("<") && extractedValue.endsWith(">") ? generateValue(extractedValue) : extractedValue;
                element.setText(extractedValue);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException(Messages.PROPERTY_NOT_FOUND.value);
            }
        }
    }

    private static String generateValue(String property) {
        if (property.toUpperCase().contains(GeneratedValues.ID.name())) {
            return String.valueOf(UUID.randomUUID());
        }
        if (property.toUpperCase().contains(GeneratedValues.DATETIME.name())) {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        } else return RandomStringUtils.randomAlphabetic(10);
    }

    private static File getCheckedFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException(String.format(Messages.FILE_NOT_FOUND.value, filePath));
        }
        return file;
    }

    public static void process(Map<ExecutionParams, String> parsedParams) {
        SAXReader reader = new SAXReader();
        Document document;
        File inputFile = getCheckedFile(parsedParams.get(ExecutionParams.INPUT));
        try {
            document = reader.read(inputFile);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(String.format(Messages.INVALID_XML.value, inputFile.getName()));
        }
        Element description = document.getRootElement();
        replace(description, readProperties(getCheckedFile(parsedParams.get(ExecutionParams.PROPERTIES))));
        writeResult(parsedParams.get(ExecutionParams.OUTPUT), description.asXML());
    }

    private static void writeResult(String outputFilePath, String result) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(result);
            writer.flush();
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(String.format(Messages.INVALID_FILE_NAME.value, outputFilePath));
        } catch (IOException e) {
            log.log(Level.SEVERE, String.format(Messages.IO_ERROR.value, "writing result"));
        }
    }
}
