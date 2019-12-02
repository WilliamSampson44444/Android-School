package edu.csumb.school2.model;


public class Enrollment {
    private int course_id;
    private double grade;
    private String letterGrade;
    
    public Enrollment(int course_id){
        this.course_id = course_id;
        grade = 0;
        letterGrade="NA";
    }
    
    public int getCourseId() { return course_id; }
    
    public double getGrade() { return grade;}
    
    public void updateGrade(double grade, String letterGrade){
        this.grade = grade;
        this.letterGrade = letterGrade;
    }
    
    public String toString(){
        return "Enrollment "+course_id;
    }
}
