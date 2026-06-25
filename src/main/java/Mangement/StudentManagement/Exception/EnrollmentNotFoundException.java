package Mangement.StudentManagement.Exception;

public class EnrollmentNotFoundException extends RuntimeException{

    public EnrollmentNotFoundException(String message){
        super(message);
    }
}
