package com.calculator;

import com.calculator.gui.CalculatorGUI;

/**
 * Main application entry point
 */
public class CalculatorApp {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            // Launch GUI
            CalculatorGUI.launch(CalculatorGUI.class, args);
        } else {
            // Command line mode
            runCommandLine(args);
        }
    }
    
    private static void runCommandLine(String[] args) {
        try {
            String expression = String.join(" ", args);
            System.out.println("Evaluating: " + expression);
            
            var result = com.calculator.core.ExpressionEvaluator.evaluate(expression);
            
            System.out.println("\n=== RESULT ===");
            System.out.printf("Value: %.6f\n", result.getResult());
            System.out.println("Postfix: " + result.getPostfix());
            System.out.printf("Time: %d ms\n", result.getTimeMs());
            
            System.out.println("\n=== STEPS ===");
            for (String step : result.getSteps()) {
                System.out.println(step);
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
