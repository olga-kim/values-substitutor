import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;
import model.ExecutionParams;
import model.Messages;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertTrue;


public class ReplaceParametersTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void checkHelp() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Messages.HELP.value);

        ExecutionParamsProcessor.parseParams(Arrays.asList("-help"));
    }

    @Test
    public void checkParams() {
        String routeDir = "src/test/resources/positive/";
        String outputFilePath = routeDir + "output-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSS")) + ".xml";

        ValuesReplacer.process(
                ExecutionParamsProcessor.parseParams(
                        Arrays.asList("-i", routeDir + "input.xml", "-p", routeDir + "example.properties", "-o", outputFilePath)));

        assertTrue("Output file was not created", new File(outputFilePath).exists());
        //TODO check output file structure
    }
}
