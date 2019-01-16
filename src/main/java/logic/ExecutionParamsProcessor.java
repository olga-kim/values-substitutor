package logic;

import model.ExecutionParams;
import model.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionParamsProcessor {

    public static Map<ExecutionParams, String> parseParams(List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equals(ExecutionParams.HELP.value)) {
                throw new IllegalArgumentException(Messages.HELP.value);
            } else {
                throw new IllegalArgumentException(Messages.INVALID_PARAMS.value);
            }
        } else {
            if (args.contains(ExecutionParams.INOUT.value)
                    && args.contains(ExecutionParams.OUTPUT.value)
                    && args.contains(ExecutionParams.PROPERTIES.value)
                    && args.size() == 6) {
                Map<ExecutionParams, String> parsedParams = new HashMap<>();

                for (int i = 0; i < args.size(); i += 2) {
                    String paramValue = args.get(i + 1);

                    switch (ExecutionParams.findByValue(args.get(i))) {
                        case INOUT:
                            parsedParams.put(ExecutionParams.INOUT, paramValue);
                            break;
                        case OUTPUT:
                            parsedParams.put(ExecutionParams.OUTPUT, paramValue);
                            break;
                        case PROPERTIES:
                            parsedParams.put(ExecutionParams.PROPERTIES, paramValue);
                            break;
                        default:
                            throw new IllegalArgumentException(Messages.INVALID_PARAMS_ORDER.value);
                    }
                }

                return parsedParams;
            } else {
                throw new IllegalArgumentException(Messages.INVALID_PARAMS.value);
            }
        }
    }
}
