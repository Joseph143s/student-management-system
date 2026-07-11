package Mangement.StudentManagement.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {

    private int id;

    private String name;

    private String email;

    private List<String> courseNames;

    private String city;

    private String departmentName;
}