package Mangement.StudentManagement.Mapper;

import Mangement.StudentManagement.DTO.Request.UserRequestDTO;
import Mangement.StudentManagement.DTO.Response.UserResponseDTO;
import Mangement.StudentManagement.Entity.User;
import Mangement.StudentManagement.Enum.Role;

public class UserMapper {

    public static User mapToUser(UserRequestDTO dto, Role role){

        User user=new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);

        return user;
    }

    public static UserResponseDTO mapToUserResponseDTO(User user){

        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
