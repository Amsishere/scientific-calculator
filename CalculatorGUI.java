package com.calculator.gui;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.calculator.core.ExpressionEvaluator;

import java.util.List;

public class CalculatorGUI extends Application {
    
    private TextField inputField;
    private TextArea outputArea;
    private TextArea stepsArea;
    private LineChart<Number, Number> chart;
    
    @Override
    public void start(Stage primaryStage) {
        // Main container
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Top: Input panel
        VBox topPanel = createInputPanel();
        root.setTop(topPanel);
        
        // Center: Results
        TabPane centerPanel = createCenterPanel();
        root.setCenter(centerPanel);
        
        // Right: Calculator buttons
        GridPane buttonPanel = createButtonPanel();
        root.setRight(buttonPanel);
        
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        primaryStage.setTitle("Scientific Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createInputPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #34495e;");
        
        Label title = new Label("Scientific Calculator");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        inputField = new TextField();
        inputField.setPromptText("Enter expression (e.g., sin(45) + 2^3 * sqrt(9))");
        inputField.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        inputField.setOnAction(e -> evaluateExpression());
        
        HBox buttonRow = new HBox(10);
        Button evaluateBtn = new Button("Evaluate");
        evaluateBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        evaluateBtn.setOnAction(e -> evaluateExpression());
        
        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        clearBtn.setOnAction(e -> clearAll());
        
        Button plotBtn = new Button("Plot Function");
        plotBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        plotBtn.setOnAction(e -> plotFunction());
        
        buttonRow.getChildren().addAll(evaluateBtn, clearBtn, plotBtn);
        
        panel.getChildren().addAll(title, inputField, buttonRow);
        return panel;
    }
    
    private TabPane createCenterPanel() {
        TabPane tabPane = new TabPane();
        
        // Result Tab
        Tab resultTab = new Tab("Result");
        resultTab.setClosable(false);
        
        VBox resultBox = new VBox(10);
        resultBox.setPadding(new Insets(15));
        
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(200);
        outputArea.setStyle("-fx-font-family: 'Monospace'; -fx-font-size: 14px;");
        
        resultBox.getChildren().add(new Label("Result:"), outputArea);
        resultTab.setContent(resultBox);
        
        // Steps Tab
        Tab stepsTab = new Tab("Calculation Steps");
        stepsTab.setClosable(false);
        
        VBox stepsBox = new VBox(10);
        stepsBox.setPadding(new Insets(15));
        
        stepsArea = new TextArea();
        stepsArea.setEditable(false);
        stepsArea.setWrapText(true);
        stepsArea.setPrefHeight(300);
        stepsArea.setStyle("-fx-font-family: 'Monospace'; -fx-font-size: 12px;");
        
        stepsBox.getChildren().add(new Label("Step-by-step:"), stepsArea);
        stepsTab.setContent(stepsBox);
        
        // Graph Tab
        Tab graphTab = new Tab("Graph");
        graphTab.setClosable(false);
        
        VBox graphBox = new VBox(10);
        graphBox.setPadding(new Insets(15));
        
        chart = new LineChart<>(new NumberAxis(), new NumberAxis());
        chart.setTitle("Function Plot");
        chart.setPrefHeight(400);
        
        graphBox.getChildren().add(chart);
        graphTab.setContent(graphBox);
        
        tabPane.getTabs().addAll(resultTab, stepsTab, graphTab);
        return tabPane;
    }
    
    private GridPane createButtonPanel() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setStyle("-fx-background-color: #34495e;");
        
        // Number buttons
        String[][] buttons = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"},
            {"(", ")", "^", "C"}
        };
        
        String[][] functions = {
            {"sin", "cos", "tan", "√"},
            {"log", "ln", "exp", "π"},
            {"abs", "deg", "rad", "e"}
        };
        
        // Add number/operator buttons
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                Button btn = createButton(buttons[row][col]);
                grid.add(btn, col, row);
            }
        }
        
        // Add function buttons
        for (int row = 0; row < functions.length; row++) {
            for (int col = 0; col < functions[row].length; col++) {
                Button btn = createFunctionButton(functions[row][col]);
                grid.add(btn, col, row + buttons.length + 1);
            }
        }
        
        return grid;
    }
    
    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(60, 50);
        btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        btn.setOnAction(e -> {
            if (text.equals("=")) {
                evaluateExpression();
            } else if (text.equals("C")) {
                clearAll();
            } else {
                inputField.appendText(text);
            }
        });
        
        return btn;
    }
    
    private Button createFunctionButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(70, 40);
        btn.setStyle("-fx-font-size: 12px;");
        
        btn.setOnAction(e -> {
            String function = text;
            if (text.equals("√")) function = "sqrt";
            else if (text.equals("π")) function = "3.141592653589793";
            else if (text.equals("e")) function = "2.718281828459045";
            
            inputField.appendText(function + "(");
        });
        
        return btn;
    }
    
    private void evaluateExpression() {
        String expression = inputField.getText().trim();
        if (expression.isEmpty()) return;
        
        try {
            ExpressionEvaluator.EvaluationResult result = 
                ExpressionEvaluator.evaluate(expression);
            
            outputArea.setText(String.format(
                "Expression: %s\n" +
                "Result: %.8f\n" +
                "Postfix: %s\n" +
                "Time: %d ms\n" +
                "Status: ✓ Success",
                expression, result.getResult(), result.getPostfix(), result.getTimeMs()
            ));
            
            stepsArea.setText(String.join("\n", result.getSteps()));
            
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
            stepsArea.setText("");
        }
    }
    
    private void plotFunction() {
        String expression = inputField.getText().trim();
        if (!expression.contains("x")) {
            outputArea.setText("To plot, expression must contain 'x' variable");
            return;
        }
        
        try {
            chart.getData().clear();
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("f(x) = " + expression);
            
            for (double x = -10; x <= 10; x += 0.5) {
                String expr = expression.replace("x", String.valueOf(x));
                double y = ExpressionEvaluator.evaluate(expr).getResult();
                series.getData().add(new XYChart.Data<>(x, y));
            }
            
            chart.getData().add(series);
            
        } catch (Exception e) {
            outputArea.setText("Plot error: " + e.getMessage());
        }
    }
    
    private void clearAll() {
        inputField.clear();
        outputArea.clear();
        stepsArea.clear();
        chart.getData().clear();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
