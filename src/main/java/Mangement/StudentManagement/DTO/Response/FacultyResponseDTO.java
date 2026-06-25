package Mangement.StudentManagement.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter

@Setter
@NoArgsConstructor
public class FacultyResponseDTO {

    private int facultyId;

    private String facultyName;

    private String email;

    private String specialization;

    private String departmentCode;


}
