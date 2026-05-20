package Mangement.StudentManagement.Exception;

public class AddressNotFoundException extends RuntimeException{
       public AddressNotFoundException(String message){
           super(message);
       }
}
