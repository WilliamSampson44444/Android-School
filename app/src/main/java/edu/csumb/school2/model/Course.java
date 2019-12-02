package edu.csumb.school2.model;

public class Course {

    private int id;
    private String title;
    private int instructor_id;
    private String room;
    private int enrollment_count;
    
    public Course(int id, String title, int instructor_id, String room){
        this.id = id;
        this.title = title;
        this.instructor_id = instructor_id;
        this.room = room;
        enrollment_count = 0;
    }

    public int getId() {
        return id;
    }
    
    public String getTitle() { return title; }
    
    public int getInstructor() { return instructor_id;}
    
    public String getLocation() { return room;}

    public void updateLocation(String newLocation) {
        room = newLocation;
        // notify listeners of this change
        School.getSchool().changeCourse(this);
    }
    
    public void updateInstructor(int id){
        instructor_id = id;
        // notify listeners of this change
        School.getSchool().changeCourse(this);
    }

    public int getEnrollmentCount() {
        return enrollment_count;
    }
    
    public void unenroll() {
        enrollment_count--;
        // notify listeners of this change
        School.getSchool().changeCourse(this);
    }
    
    public void enroll() {
        enrollment_count++;
        // notify listeners of this change
        School.getSchool().changeCourse(this);
    }
    
    public String toString() {
        String instructorName = School.getSchool().getInstructor(instructor_id).getName();
        return  id+"  "+title+" instructor:"+instructorName+" enrolled:"+ enrollment_count + " average:"+calculateCourseAverage();
    }
    
    public double calculateCourseAverage(){
        School school = School.getSchool();
        double total = 0;
        double count = 0;
        double average = 0;
        for (Student s: school.students){
            for (Enrollment e: s.getEnrollments()){
                if (e.getCourseId() == this.id && e.getGrade() != 0){
                    total = total + e.getGrade();
                    count++;
                }    
            }
        }
        if (count > 0) 
            average = total/count;
        // round to 2 decimal places
        return ((int)(average*100.0+0.5))/100.0;
    }

}
