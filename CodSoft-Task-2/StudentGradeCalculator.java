import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StudentGradeCalculator extends Application {

    private TextField nameField, branchField, regNoField, subjectsField;
    private VBox subjectsInputBox;
    private Scene formScene, marksheetScene;
    private TableView<Subject> subjectTable;
    private Label nameLabel, branchLabel, regNoLabel;

    @Override
    public void start(Stage primaryStage) {
        // ---------- Page 1: Data Entry ----------
        Label title = new Label("STUDENT DETAILS ENTRY");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        nameField = new TextField();
        nameField.setPromptText("Enter Student Name");

        branchField = new TextField();
        branchField.setPromptText("Enter Branch");

        regNoField = new TextField();
        regNoField.setPromptText("Enter Register Number");

        subjectsField = new TextField();
        subjectsField.setPromptText("Enter Number of Subjects");

        Button addSubjectsBtn = new Button("Add Subjects");
        addSubjectsBtn.setOnAction(e -> addSubjectFields());

        subjectsInputBox = new VBox(5);

        Button calculateBtn = new Button("Generate Marksheet");
        calculateBtn.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        calculateBtn.setOnAction(e -> generateMarksheet(primaryStage));

        VBox formLayout = new VBox(10, title, nameField, branchField, regNoField,
                subjectsField, addSubjectsBtn, subjectsInputBox, calculateBtn);
        formLayout.setPadding(new Insets(20));

        formScene = new Scene(formLayout, 700, 600);

        // ---------- Page 2: Marksheet ----------
        Label marksheetTitle = new Label("STUDENT MARKSHEET");
        marksheetTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #003366;");
        marksheetTitle.setAlignment(Pos.CENTER);

        nameLabel = new Label();
        branchLabel = new Label();
        regNoLabel = new Label();

        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        branchLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        regNoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox detailsBox = new VBox(5, nameLabel, branchLabel, regNoLabel);
        detailsBox.setPadding(new Insets(10));
        detailsBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        detailsBox.setPrefWidth(600);

        subjectTable = new TableView<>();
        subjectTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        subjectTable.setPlaceholder(new Label("")); // no "No content" text
        subjectTable.setFixedCellSize(30);
        subjectTable.prefHeightProperty().bind(subjectTable.fixedCellSizeProperty().multiply(subjectTable.getItems().size() + 1.01));
        subjectTable.minHeightProperty().bind(subjectTable.prefHeightProperty());
        subjectTable.maxHeightProperty().bind(subjectTable.prefHeightProperty());

        TableColumn<Subject, String> codeCol = new TableColumn<>("Subject Code");
        codeCol.setCellValueFactory(data -> data.getValue().codeProperty());

        TableColumn<Subject, String> marksCol = new TableColumn<>("Marks");
        marksCol.setCellValueFactory(data -> data.getValue().marksProperty());

        TableColumn<Subject, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(data -> data.getValue().gradeProperty());

        TableColumn<Subject, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        subjectTable.getColumns().addAll(codeCol, marksCol, gradeCol, statusCol);

        Button backBtn = new Button("Back to Home");
        backBtn.setOnAction(e -> primaryStage.setScene(formScene));

        VBox marksheetLayout = new VBox(20, marksheetTitle, detailsBox, subjectTable, backBtn);
        marksheetLayout.setAlignment(Pos.TOP_CENTER);
        marksheetLayout.setPadding(new Insets(20));
        marksheetScene = new Scene(marksheetLayout, 700, 600);

        primaryStage.setTitle("Student Marksheet");
        primaryStage.setScene(formScene);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }

    private void addSubjectFields() {
        subjectsInputBox.getChildren().clear();
        try {
            int count = Integer.parseInt(subjectsField.getText());
            for (int i = 1; i <= count; i++) {
                HBox row = new HBox(10);
                TextField codeField = new TextField();
                codeField.setPromptText("Code " + i);
                TextField markField = new TextField();
                markField.setPromptText("Marks (0-100)");
                row.getChildren().addAll(codeField, markField);
                subjectsInputBox.getChildren().add(row);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Enter valid number of subjects!");
        }
    }

    private void generateMarksheet(Stage primaryStage) {
        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        int total = 0;
        boolean allPass = true;

        for (var node : subjectsInputBox.getChildren()) {
            HBox row = (HBox) node;
            TextField codeField = (TextField) row.getChildren().get(0);
            TextField markField = (TextField) row.getChildren().get(1);

            String code = codeField.getText();
            int mark;
            try {
                mark = Integer.parseInt(markField.getText());
                if (mark < 0 || mark > 100) {
                    showAlert("Error", "Marks must be 0 to 100!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Enter valid marks!");
                return;
            }

            subjects.add(new Subject(code, mark));
            total += mark;
            if (mark < 50) allPass = false;
        }

        double average = (double) total / subjects.size();
        String overallGrade = getGrade(average);
        String overallStatus = allPass ? "PASS" : "FAIL";

        // Summary rows
        Subject totalRow = new Subject("TOTAL", total);
        totalRow.gradeProperty().set("");
        totalRow.statusProperty().set("");
        subjects.add(totalRow);

        Subject avgRow = new Subject("AVERAGE %", (int) average);
        avgRow.marksProperty().set(String.format("%.2f", average));
        avgRow.gradeProperty().set(overallGrade);
        avgRow.statusProperty().set(overallStatus);
        subjects.add(avgRow);

        subjectTable.setItems(subjects);
        subjectTable.prefHeightProperty().bind(subjectTable.fixedCellSizeProperty().multiply(subjectTable.getItems().size() + 1.01));

        // Student details
        nameLabel.setText("Name: " + nameField.getText());
        branchLabel.setText("Branch: " + branchField.getText());
        regNoLabel.setText("Register No: " + regNoField.getText());

        primaryStage.setScene(marksheetScene);
    }

    private String getGrade(double avg) {
        if (avg >= 90) return "O";
        else if (avg >= 80) return "A+";
        else if (avg >= 70) return "A";
        else if (avg >= 60) return "B+";
        else if (avg >= 50) return "B";
        else return "U";
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}