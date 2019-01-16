import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;
import org.dom4j.DocumentException;

import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        String[] params = {"-i", "src/main/resources/input.xml", "-p", "src/main/resources/example.properties", "-o", "src/main/resources/output.xml"};

        try {
            ValuesReplacer.process(new ExecutionParamsProcessor().parseParams(Arrays.asList(params))); //TODO args
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DocumentException e) {
            System.out.println("Input file contains invalid XML");
        }
    }
}
