import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;
import model.Messages;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ReplaceParametersNegativeTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        String validDataRouteDir = "src/test/resources/positive/";
        String invalidDataRouteDir = "src/test/resources/negative/";
        String nonExistentFilePath = RandomStringUtils.randomAlphabetic(5) + "/" + RandomStringUtils.randomAlphanumeric(5) + ".xml";

        return Arrays.asList(new Object[][]{
                {"Not specified params",
                        new String[]{"-i", "input.xml"},
                        Messages.INVALID_PARAMS.value},
                {"Extra params",
                        new String[]{"-i", "input.xml", "-o", "output.xml", "-p", "props.properties", "-help"},
                        Messages.INVALID_PARAMS.value},
                {"Invalid params order",
                        new String[]{"-i", "-o", "-p", "input.xml", "output.xml", "props.properties"},
                        Messages.INVALID_PARAMS_ORDER.value},
                {"Invalid input file path",
                        new String[]{"-i", nonExistentFilePath, "-o", validDataRouteDir + "output.xml", "-p", validDataRouteDir + "example.properties"},
                        String.format(Messages.FILE_NOT_FOUND.value, nonExistentFilePath)},
                {"Not valid XML in input file",
                        new String[]{"-i", invalidDataRouteDir + "not-valid-xml-input.xml", "-o", validDataRouteDir + "output.xml", "-p", validDataRouteDir + "example.properties"},
                        Messages.INVALID_XML.value},
                {"Not declared property",
                        new String[]{"-i", validDataRouteDir + "input.xml", "-o", validDataRouteDir + "output.xml", "-p", invalidDataRouteDir + "property-not-found.properties"},
                        Messages.PROPERTY_NOT_FOUND.value},
                {"Not existing output file directory",
                        new String[]{"-i", validDataRouteDir + "input.xml", "-o", nonExistentFilePath, "-p", validDataRouteDir + "example.properties"},
                        String.format(Messages.INVALID_FILE_NAME.value, nonExistentFilePath)}
        });
    }

    @Parameterized.Parameter(0)
    public String description;

    @Parameterized.Parameter(1)
    public String[] args;

    @Parameterized.Parameter(2)
    public String expectedMsg;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void checkParameters() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(expectedMsg);

        ValuesReplacer.process(ExecutionParamsProcessor.parseParams(Arrays.asList(args)));
    }

}
