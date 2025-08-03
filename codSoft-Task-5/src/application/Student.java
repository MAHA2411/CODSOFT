package application;

public class Student {
    private String name;
    private String rollNo;
    private String grade;

    public Student(String name, String rollNo, String grade) {
        this.name = name;
        this.rollNo = rollNo;
        this.grade = grade;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
