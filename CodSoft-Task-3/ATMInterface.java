import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;

public class ATMInterface extends Application {

    private HashMap<String, BankAccount> accounts = new HashMap<>();
    private BankAccount currentAccount;

    private Scene welcomeScene, createAccountScene, loginScene, menuScene, withdrawScene, depositScene, balanceScene;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ATM Interface");

        // --- Welcome Scene ---
        Label welcomeLabel = new Label("Welcome to ATM");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Button createAccountBtn = new Button("Create Account");
        Button loginBtn = new Button("Login");
        Button exitBtn = new Button("Exit");

        createAccountBtn.setOnAction(e -> primaryStage.setScene(createAccountScene(primaryStage)));
        loginBtn.setOnAction(e -> primaryStage.setScene(loginScene(primaryStage)));
        exitBtn.setOnAction(e -> primaryStage.close());

        VBox welcomeLayout = new VBox(20, welcomeLabel, createAccountBtn, loginBtn, exitBtn);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setStyle("-fx-background-color: #1E1E1E;");
        welcomeLayout.setPadding(new Insets(20));
        welcomeScene = new Scene(welcomeLayout, 400, 300);

        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private Scene createAccountScene(Stage primaryStage) {
        Label label = new Label("Create Account");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        TextField accountField = new TextField();
        accountField.setPromptText("Account Number");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");
        TextField balanceField = new TextField();
        balanceField.setPromptText("Initial Balance");
        Button createBtn = new Button("Create");
        Button backBtn = new Button("Back");

        createBtn.setOnAction(e -> {
            String acc = accountField.getText();
            String pin = pinField.getText();
            double bal = Double.parseDouble(balanceField.getText());
            accounts.put(acc, new BankAccount(acc, pin, bal));
            new Alert(Alert.AlertType.INFORMATION, "Account created successfully!").showAndWait();
            accountField.clear(); pinField.clear(); balanceField.clear();
            primaryStage.setScene(welcomeScene);
        });

        backBtn.setOnAction(e -> primaryStage.setScene(welcomeScene));

        VBox layout = new VBox(15, label, accountField, pinField, balanceField, new HBox(10, createBtn, backBtn));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1E1E1E;");
        layout.setPadding(new Insets(20));
        return new Scene(layout, 400, 300);
    }

    private Scene loginScene(Stage primaryStage) {
        Label label = new Label("Login");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        TextField accountField = new TextField();
        accountField.setPromptText("Account Number");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");
        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");

        loginBtn.setOnAction(e -> {
            String acc = accountField.getText();
            String pin = pinField.getText();
            if (accounts.containsKey(acc) && accounts.get(acc).getPin().equals(pin)) {
                currentAccount = accounts.get(acc);
                primaryStage.setScene(menuScene(primaryStage));
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Credentials").showAndWait();
            }
        });

        backBtn.setOnAction(e -> primaryStage.setScene(welcomeScene));

        VBox layout = new VBox(15, label, accountField, pinField, new HBox(10, loginBtn, backBtn));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1E1E1E;");
        layout.setPadding(new Insets(20));
        return new Scene(layout, 400, 300);
    }

    private Scene menuScene(Stage primaryStage) {
        Label menuLabel = new Label("ATM Main Menu");
        menuLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        Button withdrawBtn = new Button("Withdraw");
        Button depositBtn = new Button("Deposit");
        Button balanceBtn = new Button("Check Balance");
        Button logoutBtn = new Button("Logout");

        withdrawBtn.setOnAction(e -> primaryStage.setScene(withdrawScene(primaryStage)));
        depositBtn.setOnAction(e -> primaryStage.setScene(depositScene(primaryStage)));
        balanceBtn.setOnAction(e -> primaryStage.setScene(balanceScene(primaryStage)));
        logoutBtn.setOnAction(e -> primaryStage.setScene(welcomeScene));

        VBox layout = new VBox(20, menuLabel, withdrawBtn, depositBtn, balanceBtn, logoutBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1E1E1E;");
        layout.setPadding(new Insets(20));
        return new Scene(layout, 400, 300);
    }

    private Scene withdrawScene(Stage primaryStage) {
        Label label = new Label("Withdraw Amount");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");
        Button withdrawBtn = new Button("Withdraw");
        Button backBtn = new Button("Back");

        withdrawBtn.setOnAction(e -> {
            double amt = Double.parseDouble(amountField.getText());
            if (currentAccount.withdraw(amt)) {
                new Alert(Alert.AlertType.INFORMATION, "Withdraw Successful!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Insufficient Balance").showAndWait();
            }
            amountField.clear();
        });

        backBtn.setOnAction(e -> primaryStage.setScene(menuScene(primaryStage)));

        VBox layout = new VBox(15, label, amountField, new HBox(10, withdrawBtn, backBtn));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1E1E1E;");
        layout.setPadding(new Insets(20));
        return new Scene(layout, 400, 300);
    }

    private Scene depositScene(Stage primaryStage) {
        Label label = new Label("Deposit Amount");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");
        Button depositBtn = new Button("Deposit");
        Button backBtn = new Button("Back");

        depositBtn.setOnAction(e -> {
            double amt = Double.parseDouble(amountField.getText());
            currentAccount.deposit(amt);
            new Alert(Alert.AlertType.INFORMATION, "Deposit Successful!").showAndWait();
            amountField.clear();
        });

        backBtn.setOnAction(e -> primaryStage.setScene(menuScene(primaryStage)));

        VBox layout = new VBox(15, label, amountField, new HBox(10, depositBtn, backBtn));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1E1E1E;");
        layout.setPadding(new Insets(20));
        return new Scene(layout, 400, 300);
    }

    private Scene balanceScene(Stage primaryStage) {
        Label label = new Label("Current Balance: " + currentAccount.getBalance());
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> primaryStage.setScene(menuScene(primaryStage)));

        VBox layout = new VBox(20, label, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1E1E1E;");
        layout.setPadding(new Insets(20));
        return new Scene(layout, 400, 300);
    }

    public static void main(String[] args) {
        launch(args);
    }
}