package Mangement.StudentManagement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

@Entity

public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int facultyId;

    private String facultyName;

    private String email;

    private String specialization;

    @ManyToOne
   @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy="faculty")
    private List<Student>students;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

}
