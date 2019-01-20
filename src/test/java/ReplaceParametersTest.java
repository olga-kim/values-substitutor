import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;
import model.ExecutionParams;
import model.Messages;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.*;


public class ReplaceParametersTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void checkHelp() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Messages.HELP.value);

        ExecutionParamsProcessor.parseParams(Arrays.asList(ExecutionParams.HELP.value));
    }

    @Test
    public void checkSubstitution() throws IOException, SAXException, DocumentException {
        String routeDir = "src/test/resources/positive/";
        String propertiesFilePath = routeDir + "example.properties";
        String inputFilePath = routeDir + "input.xml";
        String outputFilePath = routeDir + "output-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSS")) + ".xml";

        ValuesReplacer.process(
                ExecutionParamsProcessor.parseParams(
                        Arrays.asList(ExecutionParams.INPUT.value, inputFilePath,
                                ExecutionParams.PROPERTIES.value, propertiesFilePath,
                                ExecutionParams.OUTPUT.value, outputFilePath)));

        assertTrue("Output file was not created", new File(outputFilePath).exists());

        Properties props = new Properties();
        try (FileReader reader = new FileReader(propertiesFilePath)) {
            props.load(reader);
        } catch (IOException e) {
            fail("IOException occurred while reading .properties file: " + e);
        }

        SAXReader reader = new SAXReader();
        String source = reader.read(inputFilePath).asXML();
        String result = reader.read(outputFilePath).asXML();

        new DetailedDiff(XMLUnit.compareXML(source, result))
                .getAllDifferences()
                .forEach(it -> {
                    String fromSource = ((Difference) it).getControlNodeDetail().getValue().replaceAll("\\{|\\}|\\$", "");
                    String fromResult = ((Difference) it).getTestNodeDetail().getValue();
                    String fromProps = props.getProperty("\"" + fromSource + "\"").replaceAll("\"", "");

                    if (!fromProps.startsWith("<") && !fromProps.endsWith(">")) {
                        assertEquals(String.format("Value '%s' was not substituted", fromSource), fromProps, fromResult);
                    }
                });

    }
}
