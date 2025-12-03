package com.calculator.core;

import java.util.Stack;

/**
 * Validates mathematical expressions
 */
public class ValidationEngine {
    
    public static void validate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty");
        }
        
        checkParentheses(expression);
        checkOperatorPlacement(expression);
        checkFunctionCalls(expression);
    }
    
    private static void checkParentheses(String expression) {
        Stack<Character> stack = new Stack<>();
        
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                stack.pop();
            }
        }
        
        if (!stack.isEmpty()) {
            throw new IllegalArgumentException("Mismatched parentheses");
        }
    }
    
    private static void checkOperatorPlacement(String expression) {
        String trimmed = expression.replaceAll("\\s+", "");
        
        // Check for consecutive operators
        for (int i = 1; i < trimmed.length(); i++) {
            char current = trimmed.charAt(i);
            char previous = trimmed.charAt(i - 1);
            
            if (isOperator(current) && isOperator(previous)) {
                // Allow some combinations like "*-" or "/-"
                if (!(previous == '*' && current == '-') &&
                    !(previous == '/' && current == '-') &&
                    !(previous == '+' && current == '-') &&
                    !(previous == '-' && current == '-')) {
                    throw new IllegalArgumentException(
                        "Consecutive operators: " + previous + current);
                }
            }
        }
    }
    
    private static void checkFunctionCalls(String expression) {
        // Simplified check - in reality would be more comprehensive
        if (expression.contains("()")) {
            throw new IllegalArgumentException("Empty function call");
        }
    }
    
    private static boolean isOperator(char c) {
        return "+-*/^%".indexOf(c) != -1;
    }
}
