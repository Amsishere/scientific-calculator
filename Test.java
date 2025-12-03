package test;

import com.calculator.core.ExpressionEvaluator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionEvaluatorTest {
    
    @Test
    public void testBasicArithmetic() {
        var result = ExpressionEvaluator.evaluate("2 + 3 * 4");
        assertEquals(14.0, result.getResult(), 0.0001);
    }
    
    @Test
    public void testParentheses() {
        var result = ExpressionEvaluator.evaluate("(2 + 3) * 4");
        assertEquals(20.0, result.getResult(), 0.0001);
    }
    
    @Test
    public void testFunctions() {
        var result = ExpressionEvaluator.evaluate("sin(0) + cos(0)");
        assertEquals(1.0, result.getResult(), 0.0001);
    }
    
    @Test
    public void testExponentiation() {
        var result = ExpressionEvaluator.evaluate("2 ^ 3 ^ 2");
        assertEquals(512.0, result.getResult(), 0.0001);
    }
    
    @Test
    public void testComplexExpression() {
        var result = ExpressionEvaluator.evaluate("sqrt(16) + log(100) * 2");
        assertEquals(8.0, result.getResult(), 0.0001);
    }
    
    @Test
    public void testInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("2 + ");
        });
    }
}
