package Mangement.StudentManagement.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CourseRequestDTO {
    private String Coursename;
    private String duration;
    private double fee;
}
