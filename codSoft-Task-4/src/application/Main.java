package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label baseLabel = new Label("Base Currency:");
        ComboBox<String> baseCurrency = new ComboBox<>();
        baseCurrency.getItems().addAll("USD", "EUR", "INR", "GBP", "JPY");
        baseCurrency.setValue("USD");

        Label targetLabel = new Label("Target Currency:");
        ComboBox<String> targetCurrency = new ComboBox<>();
        targetCurrency.getItems().addAll("USD", "EUR", "INR", "GBP", "JPY");
        targetCurrency.setValue("INR");

        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();

        Button convertButton = new Button("Convert");
        Label resultLabel = new Label();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(baseLabel, 0, 0);
        grid.add(baseCurrency, 1, 0);
        grid.add(targetLabel, 0, 1);
        grid.add(targetCurrency, 1, 1);
        grid.add(amountLabel, 0, 2);
        grid.add(amountField, 1, 2);
        grid.add(convertButton, 1, 3);
        grid.add(resultLabel, 1, 4);

        convertButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String base = baseCurrency.getValue();
                String target = targetCurrency.getValue();
                double rate = CurrencyService.getRate(base, target);
                double converted = amount * rate;
                resultLabel.setText(amount + " " + base + " = " + converted + " " + target);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid amount!");
            } catch (Exception ex) {
                resultLabel.setText("Error fetching rate!");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Currency Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
