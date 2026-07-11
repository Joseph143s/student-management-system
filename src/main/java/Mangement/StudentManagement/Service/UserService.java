package Mangement.StudentManagement.Service;

import Mangement.StudentManagement.DTO.Request.UserRequestDTO;
import Mangement.StudentManagement.DTO.Response.UserResponseDTO;
import Mangement.StudentManagement.Entity.User;

public interface UserService {

    UserResponseDTO registerStudent(UserRequestDTO dto);

    UserResponseDTO registerFaculty(UserRequestDTO dto);

    UserResponseDTO registerAdmin(UserRequestDTO dto);
}
