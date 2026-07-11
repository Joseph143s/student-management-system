package Mangement.StudentManagement.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int departmentId;

    private String departmentName;

    private String departmentCode;

    @OneToMany(mappedBy="department")
    private List<Student>students;

    @OneToMany(mappedBy="department")
    private List<Course>courses;

}
