package Mangement.StudentManagement.Exception;

public class DepartmentNotFoundException extends RuntimeException{

     public DepartmentNotFoundException(String message){
           super(message);
     }
}
