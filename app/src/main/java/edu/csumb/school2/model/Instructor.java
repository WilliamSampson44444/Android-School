package edu.csumb.school2.model;

public class Instructor {
    private int id;
    private String name;
    private String email;
    private String phone;
    
    public Instructor(int id, String name, String email, String phone){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public int getId() { return id;}
    
    public String getName() { return name; }
    
    public String toString() {
        return id+"  "+name+"  "+email+"  "+ phone;
    }
    
}