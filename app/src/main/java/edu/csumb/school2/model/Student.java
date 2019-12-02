package edu.csumb.school2.model;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private int id;
    private String name;
    private ArrayList<Enrollment> courses;
    private boolean isGraduated;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        courses = new ArrayList<Enrollment>();
        isGraduated = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * get list of course titles student is enrolled in
     *
     * @return
     */
    public List<String> getCourses() {
        ArrayList<String> list = new ArrayList();
        for (Enrollment e : courses) {
            list.add(School.getSchool().getCourse(e.getCourseId()).getTitle());
        }
        return list;
    }

    public ArrayList<Enrollment> getEnrollments() {
        return courses;
    }

    /**
     * calculate student average grade
     *
     * @return
     */
    public double average() {
        double total = 0;
        double average = 0;
        double count = 0;
        for (Enrollment e : courses) {
            if (e.getGrade() != 0) {
                total = total + e.getGrade();
                count++;
            }
        }
        if (count > 0) {
            average = total / count;
        }
        average = ((int) average * 100 + 0.5) / 100.0; // round 2 place
        return average;
    }

    public void enroll(Course c) {
        Enrollment e = new Enrollment(c.getId());
        courses.add(e);
        c.enroll();
    }

    public boolean updateGrade(double grade, String letterGrade, int course_id) {
        Enrollment e = findCourse(course_id);
        if (e == null) {
            return false;
        } else {
            e.updateGrade(grade, letterGrade);
            return true;
        }
    }

    private Enrollment findCourse(int course_id) {
        for (Enrollment e : courses) {
            if (e.getCourseId() == course_id) {
                return e;
            }
        }
        return null;
    }

    public boolean isGraduated() {
        return isGraduated;
    }

    public void setGraduated() {
        isGraduated = true;
        for (Enrollment e : courses) {
            Course c = School.getSchool().getCourse(e.getCourseId());
            c.unenroll();
        }
        courses.clear();
    }

    public String toString() {
        if (isGraduated) {
            return id + " " + name + " Graduated";
        } else {
            return id + " " + name + " GPA:" + average() + " number courses:" + courses.size();
        }
    }

}
