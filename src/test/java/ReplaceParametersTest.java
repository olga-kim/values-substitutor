import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;
import model.ExecutionParams;
import model.Messages;
import org.dom4j.DocumentException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
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

        ExecutionParamsProcessor executionParamsProcessor = new ExecutionParamsProcessor();
        executionParamsProcessor.parseParams(Arrays.asList("-help"));
    }

    @Test
    public void checkParams() throws DocumentException {
        ExecutionParamsProcessor executionParamsProcessor = new ExecutionParamsProcessor();
        Map<ExecutionParams, String> parsedParams = executionParamsProcessor.parseParams(Arrays.asList("-i", "src/test/resources/input.xml", "-p", "src/test/resources/example.properties", "-o", "src/test/resources/output.xml"));

        ValuesReplacer.process(parsedParams);

        assertTrue("", new File("src/test/resources/output.xml").exists());
        //TODO check output file structure
    }
}
