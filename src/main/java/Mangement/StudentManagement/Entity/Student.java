package Mangement.StudentManagement.Entity;
import jakarta.persistence.*;
import java.util.*;
import Mangement.StudentManagement.Entity.Course;
@Entity
public class Student {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   private String name;
   private String email;
   @ManyToOne
    private Course course;
   @ManyToMany
   @JoinTable(
           name="student_course",
           joinColumns=@JoinColumn(name="student_id"),
           inverseJoinColumns=@JoinColumn(name= "course_id")
   )
   private List<Course>courses;
   @OneToOne
    private Address address;
   public Student(){}
    public Student(String name,String email,Course course,Address address){
       this.name=name;
       this.email=email;
       this.course=course;
       this.address=address;
    }
    public void setId(Integer id){
        this.id=id;
    }

    public Integer getId(){
        return id;
    }
    public void setName(String name){
       this.name=name;
    }
    public String getName(){
       return name;

    }
    public void setEmail(String email){
       this.email=email;
    }
    public String getEmail(){return email;}
    public void setCourse(Course course){
       this.course=course;
   }
    public Course getCourse(){
       return course;
    }
    public void setAddress(Address address){
       this.address=address;
    }
    public Address getAddress(){
       return address;
    }

}