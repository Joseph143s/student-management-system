package Mangement.StudentManagement.Service;

import Mangement.StudentManagement.DTO.Request.LoginRequestDTO;
import Mangement.StudentManagement.DTO.Response.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO dto);

}