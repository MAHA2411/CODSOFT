import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SmartNumberTrainer extends Application {

    Stage window;
    Scene welcomeScene, gameScene;
    String username;
    int numberToGuess, upperLimit, maxAttempts, attemptsLeft, hintLevel, hintPenalty, score;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        buildWelcomeScene();
        window.setScene(welcomeScene);
        window.setTitle("Smart Number Trainer");
        window.show();
    }

    private void buildWelcomeScene() {
        Label titleLabel = new Label("🎓 Smart Number Trainer");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label nameLabel = new Label("Enter your name:");
        TextField nameInput = new TextField();

        Label levelLabel = new Label("Select Difficulty Level:");
        ComboBox<String> levelBox = new ComboBox<>();
        levelBox.getItems().addAll("Easy (1-100)", "Medium (1-500)", "Hard (1-1000)");
        levelBox.setValue("Easy (1-100)");

        Button startButton = new Button("🎮 Start Game");
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        startButton.setOnAction(e -> {
            username = nameInput.getText();
            String level = levelBox.getValue();
            if (username.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter your name!");
                alert.show();
            } else {
                setupGameScene(level);
                window.setScene(gameScene);
            }
        });

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #f0f8ff;");
        layout.getChildren().addAll(titleLabel, nameLabel, nameInput, levelLabel, levelBox, startButton);
        welcomeScene = new Scene(layout, 400, 350);
    }

    private void setupGameScene(String level) {
        // Set level-based range and attempts
        if (level.startsWith("Easy")) {
            upperLimit = 100;
            maxAttempts = 15;
        } else if (level.startsWith("Medium")) {
            upperLimit = 500;
            maxAttempts = 12;
        } else {
            upperLimit = 1000;
            maxAttempts = 10;
        }

        numberToGuess = (int) (Math.random() * upperLimit) + 1;
        attemptsLeft = maxAttempts;
        hintLevel = 1;
        hintPenalty = 0;
        score = 0;

        Label greeting = new Label("Hello " + username + " 👋");
        Label instruction = new Label("Guess a number between 1 and " + upperLimit);
        Label hintLabel = new Label("🧠 Hint 1 (free): " + getHint(hintLevel));
        Label extraHintLabel = new Label();
        Label feedbackLabel = new Label();
        Label attemptsLabel = new Label("Attempts left: " + attemptsLeft);

        TextField guessInput = new TextField();
        guessInput.setPromptText("Enter your guess");

        Button guessBtn = new Button("✅ Submit Guess");
        guessBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        Button extraHintBtn = new Button("🧠 Get Extra Hint (-1 or -2)");
        extraHintBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        guessBtn.setOnAction(e -> {
            String input = guessInput.getText();
            if (!input.matches("\\d+")) {
                feedbackLabel.setText("⚠️ Please enter a valid number.");
                return;
            }

            int guess = Integer.parseInt(input);
            guessInput.clear();
            attemptsLeft--;
            attemptsLabel.setText("Attempts left: " + attemptsLeft);

            if (guess == numberToGuess) {
                score = 10 - hintPenalty;
                feedbackLabel.setText("🎉 Correct! You scored " + score + " points.");
                guessBtn.setDisable(true);
                extraHintBtn.setDisable(true);
                showSummaryScene("🎉 You won!", maxAttempts - attemptsLeft);
            } if (guess < numberToGuess) {
    feedbackLabel.setText("📉 Too low! Try a higher number.");
} else {
    feedbackLabel.setText("📈 Too high! Try a lower number.");
}
            if (attemptsLeft == 0 && guess != numberToGuess) {
                feedbackLabel.setText("❌ Out of attempts! Number was: " + numberToGuess);
                guessBtn.setDisable(true);
                extraHintBtn.setDisable(true);
                showSummaryScene("😢 You lost!", maxAttempts);
            }
        });

        extraHintBtn.setOnAction(e -> {
            if (hintLevel == 1) {
                hintLevel++;
                hintPenalty += 1;
                extraHintLabel.setText("Hint 2: " + getHint(hintLevel));
            } else if (hintLevel == 2) {
                hintLevel++;
                hintPenalty += 2;
                extraHintLabel.setText("Hint 3: " + getHint(hintLevel));
                extraHintBtn.setDisable(true);
            } else {
                extraHintLabel.setText("No more hints available.");
            }
        });

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #fff8dc;");
        layout.getChildren().addAll(greeting, instruction, hintLabel, extraHintBtn, extraHintLabel,
                guessInput, guessBtn, feedbackLabel, attemptsLabel);
        gameScene = new Scene(layout, 450, 450);
    }

    private String getHint(int stage) {
        return switch (stage) {
            case 1 -> (numberToGuess % 2 == 0 ? "The number is even." : "The number is odd.");
            case 2 -> "Number is between " + (numberToGuess - 25) + " and " + (numberToGuess + 25);
case 3 -> "First digit of the number is " + Integer.toString(numberToGuess).charAt(0);
            default -> "No hint available";
        };
    }

    private void showSummaryScene(String result, int attemptsUsed) {
        Label summaryLabel = new Label("🏁 Game Summary");
        summaryLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label name = new Label("Player: " + username);
        Label scoreLabel = new Label("Final Score: " + score);
        Label attempts = new Label("Attempts Used: " + attemptsUsed);
        Label hintsUsed = new Label("Hint Penalty: -" + hintPenalty + " points");
        Label message = new Label(result);

        Button playAgain = new Button("🔁 Play Again");
        playAgain.setOnAction(e -> window.setScene(welcomeScene));

        Button exit = new Button("❌ Exit");
        exit.setOnAction(e -> window.close());

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #e3f2fd;");
        layout.getChildren().addAll(summaryLabel, name, scoreLabel, attempts, hintsUsed, message, playAgain, exit);
        Scene summaryScene = new Scene(layout, 400, 400);
        window.setScene(summaryScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}