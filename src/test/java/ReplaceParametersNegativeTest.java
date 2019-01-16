import logic.ExecutionParamsProcessor;
import model.Messages;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

//@RunWith(Parameterized.class)
public class ReplaceParametersNegativeTest {

    /*@Parameterized.Parameters
    public static Collection<Object[]> data() {
           *//*create and return a Collection
             of Objects arrays here.
             Each element in each array is
             a parameter to your constructor.
            *//*

    }

    private int a,b,c;


    public ReplaceParametersTest(int a, int b, int c) {
        this.a= a;
        this.b = b;
        this.c = c;
    }*/

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void checkHelp(){
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Messages.HELP.value);

        ExecutionParamsProcessor executionParamsProcessor = new ExecutionParamsProcessor();
        executionParamsProcessor.parseParams(Arrays.asList("-help"));
    }
}
