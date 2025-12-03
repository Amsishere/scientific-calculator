package com.calculator.core;

import java.util.*;

/**
 * Converts infix expressions to postfix using Shunting Yard algorithm
 */
public class InfixToPostfixConverter {
    
    private static final Map<String, Integer> PRECEDENCE = Map.of(
        "+", 1, "-", 1, "*", 2, "/", 2, "^", 3, "%", 2
    );
    
    public static String convert(List<String> tokens) {
        Stack<String> operators = new Stack<>();
        List<String> output = new ArrayList<>();
        
        for (String token : tokens) {
            if (ExpressionEvaluator.isNumeric(token)) {
                output.add(token);
            } else if (ExpressionEvaluator.isOperator(token)) {
                while (!operators.isEmpty() && 
                       !operators.peek().equals("(") &&
                       hasHigherPrecedence(operators.peek(), token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                operators.pop(); // Remove "("
            }
        }
        
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
        
        return String.join(" ", output);
    }
    
    private static boolean hasHigherPrecedence(String op1, String op2) {
        return PRECEDENCE.getOrDefault(op1, 0) >= PRECEDENCE.getOrDefault(op2, 0);
    }
}
