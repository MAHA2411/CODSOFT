package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.IOException;

public class Main extends Application {

    private TableView<Student> table;
    private ObservableList<Student> students;

    @Override
    public void start(Stage stage) {
        // Load students from file
        try {
            students = FXCollections.observableArrayList(StudentData.loadStudents());
        } catch (IOException e) {
            students = FXCollections.observableArrayList();
        }

        // Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search by Name or Roll No");

        // TableView
        table = new TableView<>(students);
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Student, String> rollCol = new TableColumn<>("Roll No");
        rollCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        TableColumn<Student, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        table.getColumns().addAll(nameCol, rollCol, gradeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Form Inputs
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField rollField = new TextField();
        rollField.setPromptText("Roll No");
        TextField gradeField = new TextField();
        gradeField.setPromptText("Grade");

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button delBtn = new Button("Delete");

        HBox formBox = new HBox(10, nameField, rollField, gradeField, addBtn, editBtn, delBtn);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(10));

        // Filter table when searching
        searchField.textProperty().addListener((obs, old, newValue) -> {
            if (newValue.isEmpty()) {
                table.setItems(students);
            } else {
                ObservableList<Student> filtered = FXCollections.observableArrayList();
                for (Student s : students) {
                    if (s.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                        s.getRollNo().toLowerCase().contains(newValue.toLowerCase())) {
                        filtered.add(s);
                    }
                }
                table.setItems(filtered);
            }
        });

        // Add Student
        addBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty() || rollField.getText().isEmpty() || gradeField.getText().isEmpty()) {
                showAlert("Validation Error", "All fields are required!");
                return;
            }
            for (Student s : students) {
                if (s.getRollNo().equalsIgnoreCase(rollField.getText())) {
                    showAlert("Duplicate Roll No", "A student with this roll number already exists!");
                    return;
                }
            }
            students.add(new Student(nameField.getText(), rollField.getText(), gradeField.getText()));
            clearFields(nameField, rollField, gradeField);
        });

        // Edit Student
        editBtn.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a student to edit");
                return;
            }
            if (!nameField.getText().isEmpty()) selected.setName(nameField.getText());
            if (!rollField.getText().isEmpty()) selected.setRollNo(rollField.getText());
            if (!gradeField.getText().isEmpty()) selected.setGrade(gradeField.getText());
            table.refresh();
            clearFields(nameField, rollField, gradeField);
        });

        // Delete Student
        delBtn.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a student to delete");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.YES) {
                students.remove(selected);
            }
        });

        VBox layout = new VBox(10, searchField, table, formBox);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 700, 500);


        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) field.clear();
    }

    @Override
    public void stop() {
        try {
            StudentData.saveStudents(students);
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
