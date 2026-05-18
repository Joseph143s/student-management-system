package Mangement.StudentManagement.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StudentRequestDTO {
    private String name;
    private String email;
    private int courseId;
    private int addressId;

}
