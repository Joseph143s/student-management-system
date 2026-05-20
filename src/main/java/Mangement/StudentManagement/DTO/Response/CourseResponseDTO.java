package Mangement.StudentManagement.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseResponseDTO {
    private int courseId;
    private String coursename;
    private String duration;
    private double fee;
}
