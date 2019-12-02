package edu.csumb.school2.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;



public class School {

    private String name;
    public ArrayList<Student> students;
    public ArrayList<Course> catalog;
    public ArrayList<Instructor> faculty;
    
    private static School _school = null;
    
    public static School getSchool() { return _school;}
    
    public static void createSchool(String name){
        if (_school == null){
            _school = new School(name);
        }
    }
    
    private  School(String name) {
        this.name = name;
        students = new ArrayList<Student>();
        catalog = new ArrayList<Course>();
        faculty = new ArrayList<Instructor>();
        _school = this;
    }

    public void readData(String filename) {
        try {
            Scanner f = new Scanner( new File(filename));
            
            // read instructor data
            int icount = f.nextInt();
            f.nextLine();  // skip to next reocrd
            for (int i=0; i<icount; i++){
                String line = f.nextLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                int id = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                String email = st.nextToken();
                String phone = st.nextToken();
                addInstructor(id, name, email, phone);
                
            }
            // read course data
            int ccount = f.nextInt();
            f.nextLine();  // skip to next reocrd
            for (int i=0; i<ccount; i++){
                String line = f.nextLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                int id = Integer.parseInt(st.nextToken());
                String title = st.nextToken();
                int instructor_id = Integer.parseInt(st.nextToken());
                String room = st.nextToken();
                addCourse(id, title, instructor_id, room);
            }
          
            // read student records
            int scount = f.nextInt();
            f.nextLine();  // skip to next reocrd
            for (int i=0; i<scount; i++){
                String line = f.nextLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                int id = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                int course_id = Integer.parseInt(st.nextToken());
                double grade = Double.parseDouble(st.nextToken());
                String letterGrade = st.nextToken();
                addStudent(id, name, course_id, grade, letterGrade);                
            }
        
        } catch (Exception e){
            System.out.println("Exeption in readData "+e.getMessage()+" "+filename);
            return;
        }
    }

    public void schoolInfo() {
        System.out.println("School: " + name);
        System.out.println("Instructors");
        for(int i = 0; i < faculty.size(); i++){
            System.out.println(faculty.get(i).getName());
        }
        System.out.println();
        System.out.println("Courses");
        for(int i = 0; i < catalog.size(); i++){
            System.out.println(catalog.get(i).getTitle());
        }
        System.out.println();
        System.out.println("Students");
        for(int i = 0; i < students.size(); i++){
            for(int j = 0; j < students.get(i).getCourses().size(); j++){
                System.out.println(students.get(i).getName() + ": " + students.get(i).getCourses().get(j));
            }
        }
    }
    


    public boolean addInstructor(int id, String name, String email, String phone) {
        if (getInstructor(id) != null) {
            return false;  // duplicate id
        }
        Instructor t = new Instructor(id, name, email, phone);
        if (faculty.size()==0 || faculty.get(faculty.size()-1).getId()<id){
            faculty.add(t);  // insert at end of list
        } else {
            for (int i=0; i<faculty.size(); i++){
                if (faculty.get(i).getId()>= id){
                    faculty.add(i, t);
                    break;
                }
            }
        }
        return true;
    }
    
    public Instructor getInstructor(int id){
        for (Instructor in : faculty){
            if (in.getId()==id) return in;
        }
        return null;
    }
    

    public boolean addCourse(int id, String title, int instructor, String room) {
        if (getCourse(id)!=null) return false;  // dup id
        if (getInstructor(instructor)==null) return false; // invalid instructor id
        Course c = new Course(id, title, instructor, room);
        if (catalog.size()==0 || catalog.get(catalog.size()-1).getId() < id) 
            catalog.add(c);  // add at end 
        for (int i=0; i<catalog.size(); i++){
            if (catalog.get(i).getId() > id) {
                catalog.add(i, c);
                break;
            }
        }
        return true;
    }
    
    public void changeCourse(Course c){
        int k = catalog.indexOf(c);
        catalog.set(k, c);
    }

    public void courseInfo(int id) {
        System.out.println(getCourse(id));
    }

    public Course getCourse(int id) {
        for (Course c: catalog){
            if (c.getId() == id) return c;
        }
        return null;
    }

    public void courseInfo() {
        for(int i = 0; i < catalog.size(); i++){
            System.out.println(catalog.get(i));
        }
    }

    public boolean deleteCourse(int id) {
        Course course = getCourse(id);
        if (course != null) {
            if (course.getEnrollmentCount() == 0) {
                catalog.remove(course);
                return true;
            }
        }
        System.out.println("Course "+id+"  not deleted"+course.getEnrollmentCount());
        return false;
    }

    /**
     * find or create new Student, enroll in course, and give grade
     * @param id
     * @param name
     * @param course_id
     * @param grade
     * @param letterGrade
     * @return false if student id already in use by another student, course id invalid.
     */
    public boolean addStudent(int id, String name, int course_id, double grade, String letterGrade) {
        Student s = getStudentInfo(id);
        if (s != null) { 
             if (!s.getName().equals(name)) {
                 System.out.println("Add student - invalid student id "+id);
                return false;  // id is used by different student
            }
        } else {
            s = new Student(id, name);
        }
        
        Course c = getCourse(course_id);
        if (c == null) {
            System.out.println("Add student - invalid course id "+id+" "+course_id);
            return false;  // invalid course id
        }
        if (students.size()==0 || students.get(students.size()-1).getId() < id) 
            students.add(s);  // add at end 
        // insert student into sorted list
        for (int i=0; i<students.size(); i++){
            if (students.get(i).getId() > id) {
                students.add(i, s);
                break;
            }
        }
        s.enroll(c);
        s.updateGrade(grade, letterGrade, c.getId());
        return true;
    }

    public Student getStudentInfo(int id) {
        for (Student s: students){
            if (id == s.getId()) return s;
        }
        return null;
    }

    public boolean graduateStudent(int id) {
        Student s = getStudentInfo(id);
        if (s != null && !s.isGraduated()) {
            s.setGraduated();
            return true;
        } else {
            return false;
        }
    }
}
