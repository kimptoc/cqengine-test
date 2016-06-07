import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateBaseBase {
    protected static void endOfTest(Future<Boolean> result1, Future<Boolean> result2, Future<Boolean> result3) {
        try {
            assertThis(result1.get());
            assertThis(result2.get());
            assertThis(result3.get());
        } catch (InterruptedException| ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Test completed ok!");
    }

    private static void assertThis(Boolean failIfTrue) {
        if (failIfTrue) throw new RuntimeException("Assertion true :(");
    }
}
