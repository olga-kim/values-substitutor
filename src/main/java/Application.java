import logic.ExecutionParamsProcessor;
import logic.ValuesReplacer;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static Logger log = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        try {
            ValuesReplacer.process(ExecutionParamsProcessor.parseParams(Arrays.asList(args)));
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}
