package Mangement.StudentManagement.Entity;
import jakarta.persistence.*;
import java.util.*;
import Mangement.StudentManagement.Entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    private String coursename;
    private String duration;
    private double fee;
    @ManyToMany(mappedBy="courses")
    private List<Student>students;

}
