import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Subject {
    private final StringProperty code;
    private final StringProperty marks;
    private final StringProperty grade;
    private final StringProperty status;

    public Subject(String code, int marks) {
        this.code = new SimpleStringProperty(code);
        this.marks = new SimpleStringProperty(String.valueOf(marks));
        this.grade = new SimpleStringProperty(calculateGrade(marks));
        this.status = new SimpleStringProperty(marks >= 50 ? "PASS" : "FAIL");
    }

    private String calculateGrade(int mark) {
        if (mark >= 90) return "O";
        else if (mark >= 80) return "A+";
        else if (mark >= 70) return "A";
        else if (mark >= 60) return "B+";
        else if (mark >= 50) return "B";
        else return "U";
    }

    public StringProperty codeProperty() { return code; }
    public StringProperty marksProperty() { return marks; }
    public StringProperty gradeProperty() { return grade; }
    public StringProperty statusProperty() { return status; }
}