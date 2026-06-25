package Mangement.StudentManagement.Exception;

import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCourseNotFound(CourseNotFoundException ex) {

       return new ResponseEntity<>(
                      ApiResponseBuilder.error(
                              404,
                              ex.getMessage(),
                              null
                      ),
               HttpStatus.NOT_FOUND
       );
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleStudentNotFound(StudentNotFoundException ex) {
        return new ResponseEntity<>(ApiResponseBuilder.error(404,
                ex.getMessage(),
                null
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleAddressNotFound(AddressNotFoundException ex) {

        return new ResponseEntity<>(ApiResponseBuilder.error(404,
                ex.getMessage(),
                null
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String,String>>>handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String>errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        });
        return new ResponseEntity<>(
                 ApiResponseBuilder.error(
                         400,"Validation failed"
                         , errors
                 ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiResponse<String>>
    handleDepartmentNotFound(DepartmentNotFoundException ex){

        return new ResponseEntity<>(
                ApiResponseBuilder.error(
                        404,
                        ex.getMessage(),
                        null
                ),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(FacultyNotFoundException.class)
    public ResponseEntity<ApiResponse<String>>
    handleFacultyNotFound(FacultyNotFoundException ex){

        return new ResponseEntity<>(
                ApiResponseBuilder.error(
                        404,
                        ex.getMessage(),
                        null
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EnrollmentNotFoundException.class)
    public ResponseEntity<ApiResponse<String>>
    handleEnrollmentNotFound(EnrollmentNotFoundException ex){

        return new ResponseEntity<>(
                ApiResponseBuilder.error(
                        404,
                        ex.getMessage(),
                        null
                ),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>>
    handleUserAlreadyExists(UserAlreadyExistsException ex){

        return new ResponseEntity<>(
                ApiResponseBuilder.error(
                        409,
                        ex.getMessage(),
                        null
                ),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {

        return new ResponseEntity<>(ApiResponseBuilder.error(
                500,
                "something went wrong",
        ex.getMessage()
        ),
        HttpStatus.INTERNAL_SERVER_ERROR);
    }
}