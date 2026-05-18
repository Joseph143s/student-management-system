package Mangement.StudentManagement.Entity;
import jakarta.persistence.*;
import java.util.*;
import Mangement.StudentManagement.Entity.Student;
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    private String coursename;
    private String duration;
    private double fee;
    @ManyToMany(mappedBy="courses")
    private List<Student>students;
    public Course(){}
    public Course(String coursename,String duration,double fee){
        this.coursename=coursename;
        this.duration=duration;
        this.fee=fee;
    }
    public void setCourseId(int courseId){
        this.courseId=courseId;
    }

    public int getCourseId(){
        return courseId;
    }

public void setCoursename(String coursename){

        this.coursename=coursename;
}
public String getCoursename(){

        return coursename;
}

public void setDuration(String duration){
    this.duration=duration;
}
public String getDuration(){
    return duration;

}
public void setFee(Double fee){
    this.fee=fee;
}

public Double getFee(){
    return fee;
}
}
