package com.calculator.core;

import java.util.*;
import java.util.function.Function;

/**
 * Advanced expression evaluator supporting scientific functions
 */
public class ExpressionEvaluator {
    
    private static final Map<String, Function<Double, Double>> FUNCTIONS = 
        new HashMap<>();
    
    private static final Map<String, Integer> PRECEDENCE = Map.of(
        "+", 1, "-", 1, "*", 2, "/", 2, "^", 3, "%", 2
    );
    
    static {
        // Basic math functions
        FUNCTIONS.put("sin", Math::sin);
        FUNCTIONS.put("cos", Math::cos);
        FUNCTIONS.put("tan", Math::tan);
        FUNCTIONS.put("log", Math::log10);
        FUNCTIONS.put("ln", Math::log);
        FUNCTIONS.put("sqrt", Math::sqrt);
        FUNCTIONS.put("abs", Math::abs);
        FUNCTIONS.put("exp", Math::exp);
        FUNCTIONS.put("ceil", Math::ceil);
        FUNCTIONS.put("floor", Math::floor);
        FUNCTIONS.put("rad", Math::toRadians);
        FUNCTIONS.put("deg", Math::toDegrees);
    }
    
    public static class EvaluationResult {
        private final double result;
        private final String postfix;
        private final List<String> steps;
        private final long timeMs;
        
        public EvaluationResult(double result, String postfix, 
                               List<String> steps, long timeMs) {
            this.result = result;
            this.postfix = postfix;
            this.steps = steps;
            this.timeMs = timeMs;
        }
        
        public double getResult() { return result; }
        public String getPostfix() { return postfix; }
        public List<String> getSteps() { return steps; }
        public long getTimeMs() { return timeMs; }
    }
    
    public static EvaluationResult evaluate(String expression) {
        long startTime = System.currentTimeMillis();
        List<String> steps = new ArrayList<>();
        
        try {
            // Step 1: Validate
            ValidationEngine.validate(expression);
            steps.add("✓ Expression validated");
            
            // Step 2: Tokenize
            List<String> tokens = tokenize(expression);
            steps.add("✓ Tokenized: " + tokens);
            
            // Step 3: Convert to postfix
            String postfix = InfixToPostfixConverter.convert(tokens);
            steps.add("✓ Postfix notation: " + postfix);
            
            // Step 4: Evaluate
            double result = evaluatePostfix(postfix, steps);
            steps.add("✓ Result: " + result);
            
            long endTime = System.currentTimeMillis();
            return new EvaluationResult(result, postfix, steps, endTime - startTime);
            
        } catch (Exception e) {
            throw new CalculationException("Evaluation failed: " + e.getMessage(), e);
        }
    }
    
    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isWhitespace(c)) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else if (isOperator(c) || c == '(' || c == ')' || c == ',') {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            tokens.add(current.toString());
        }
        
        return tokens;
    }
    
    private static double evaluatePostfix(String postfix, List<String> steps) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");
        
        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
                steps.add("  Push " + token + " → Stack: " + stack);
            } else if (FUNCTIONS.containsKey(token)) {
                if (stack.isEmpty()) throw new IllegalArgumentException(
                    "Missing argument for function: " + token);
                
                double arg = stack.pop();
                double result = FUNCTIONS.get(token).apply(arg);
                stack.push(result);
                steps.add("  Apply " + token + "(" + arg + ") = " + result);
            } else if (isOperator(token)) {
                if (stack.size() < 2) throw new IllegalArgumentException(
                    "Insufficient operands for: " + token);
                
                double b = stack.pop();
                double a = stack.pop();
                double result = applyOperator(a, b, token);
                stack.push(result);
                steps.add("  Apply " + a + " " + token + " " + b + " = " + result);
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        
        return stack.pop();
    }
    
    private static boolean isOperator(char c) {
        return "+-*/^%".indexOf(c) != -1;
    }
    
    private static boolean isOperator(String str) {
        return PRECEDENCE.containsKey(str);
    }
    
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static double applyOperator(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case "^": return Math.pow(a, b);
            case "%": return a % b;
            default: throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }
    
    public static void registerFunction(String name, Function<Double, Double> function) {
        FUNCTIONS.put(name, function);
    }
}

class CalculationException extends RuntimeException {
    public CalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
