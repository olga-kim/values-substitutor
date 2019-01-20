import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;
import model.ExecutionParams;
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
        String baseRouteDir = "src/test/resources/";
        String validDataRouteDir = baseRouteDir + "positive/";
        String invalidDataRouteDir = baseRouteDir + "negative/";

        String nonExistentFilePath = RandomStringUtils.randomAlphabetic(5) + "/" + RandomStringUtils.randomAlphanumeric(5) + ".xml";

        String validInputFilePath = validDataRouteDir + FileNames.INPUT.value;
        String validOutputFilePath = validDataRouteDir + FileNames.OUTPUT.value;
        String validPropertiesFilePath = validDataRouteDir + FileNames.PROPERTIES.value;

        return Arrays.asList(new Object[][]{
                {"Not specified params",
                        new String[]{ExecutionParams.INPUT.value, FileNames.INPUT.value},
                        Messages.INVALID_PARAMS.value},
                {"Extra params",
                        new String[]{ExecutionParams.INPUT.value, FileNames.INPUT.value,
                                ExecutionParams.OUTPUT.value, FileNames.OUTPUT.value,
                                ExecutionParams.PROPERTIES.value, FileNames.PROPERTIES.value,
                                ExecutionParams.HELP.value},
                        Messages.INVALID_PARAMS.value},
                {"Invalid params order",
                        new String[]{ExecutionParams.INPUT.value, ExecutionParams.OUTPUT.value, ExecutionParams.PROPERTIES.value,
                                FileNames.INPUT.value, FileNames.OUTPUT.value, FileNames.PROPERTIES.value},
                        Messages.INVALID_PARAMS_ORDER.value},
                {"Invalid input file path",
                        new String[]{ExecutionParams.INPUT.value, nonExistentFilePath,
                                ExecutionParams.OUTPUT.value, validOutputFilePath,
                                ExecutionParams.PROPERTIES.value, validPropertiesFilePath},
                        String.format(Messages.FILE_NOT_FOUND.value, nonExistentFilePath)},
                {"Not valid XML in input file",
                        new String[]{ExecutionParams.INPUT.value, invalidDataRouteDir + FileNames.NOT_VALID_XML_INPUT.value,
                                ExecutionParams.OUTPUT.value, validOutputFilePath,
                                ExecutionParams.PROPERTIES.value, validPropertiesFilePath},
                        String.format(Messages.INVALID_XML.value, FileNames.NOT_VALID_XML_INPUT.value)},
                {"Not declared property",
                        new String[]{ExecutionParams.INPUT.value, validInputFilePath,
                                ExecutionParams.OUTPUT.value, validOutputFilePath,
                                ExecutionParams.PROPERTIES.value, invalidDataRouteDir + FileNames.PROPERTY_NOT_FOUND.value},
                        Messages.PROPERTY_NOT_FOUND.value},
                {"Not existing output file directory",
                        new String[]{ExecutionParams.INPUT.value, validInputFilePath,
                                ExecutionParams.OUTPUT.value, nonExistentFilePath,
                                ExecutionParams.PROPERTIES.value, validPropertiesFilePath},
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
    public void checkSubstitution() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(expectedMsg);

        ValuesReplacer.process(ExecutionParamsProcessor.parseParams(Arrays.asList(args)));
    }

}

enum FileNames {
    INPUT("input.xml"),
    OUTPUT("output.xml"),
    PROPERTIES("example.properties"),
    NOT_VALID_XML_INPUT("not-valid-xml-input.xml"),
    PROPERTY_NOT_FOUND("property-not-found.properties");

    public String value;

    FileNames(String value) {
        this.value = value;
    }
}
