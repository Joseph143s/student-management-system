package Mangement.StudentManagement.Controller;
import Mangement.StudentManagement.Service.*;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.*;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/students")
@Tag(
        name= "Student APIs",
        description = "Operations related to Student Management"
)
public class StudentController {

    private final StudentService studentservice;
    public StudentController(StudentService studentservice){

        this.studentservice=studentservice;
    }

    @Operation(summary=" Save Students")

    @PostMapping

    public ResponseEntity<ApiResponse<StudentResponseDTO>> saveStudent(
            @Valid @RequestBody StudentRequestDTO dto){

        StudentResponseDTO response= studentservice.saveStudent(dto);

        return new ResponseEntity<>(
                ApiResponseBuilder.success(201,
                        "Student created successfully",
                        response),HttpStatus.CREATED
        );
    }
    @Operation(summary=" get All students")

    @GetMapping

    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents(){
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student fetched successfully",
                        studentservice.getAllStudents()
                ));
    }

    @Operation(summary="get Student by id")

    @GetMapping("/{id}")

    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable int id){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student fetched successfully",
                        studentservice.getStudentById(id)));
    }
    @Operation(summary="get student address by student id")

    @GetMapping("/{id}/address")

    public ResponseEntity<ApiResponse<AddressResponseDTO>> getStudentAddressById(@PathVariable int id){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Fetched student address successfully",
                        studentservice.getStudentAddressById(id)
                ));
    }
    @Operation(summary="update student")

    @PutMapping("/{id}")

     public ResponseEntity<ApiResponse<StudentResponseDTO>>updateStudent(
             @PathVariable int id ,
             @Valid @RequestBody StudentRequestDTO dto){
        StudentResponseDTO updated=  studentservice.updateStudent(id,dto);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(200,
                        "Student updated successfully",
                      updated
                )
        );

    }

    @Operation(summary="delete student")

    @DeleteMapping("/{id}")

    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable int id){
        studentservice.deleteStudentById(id);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student deleted Successfully",
                null
    ));
    }
    // CHECK STUDENT EXISTS
    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @PathVariable int id) {

        return ResponseEntity.ok(
               ApiResponseBuilder.success( 200,
                "Existence checked successfully",
                studentservice.existsById(id)
        ));
    }



    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>>countStudents() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student count fetched successfully",
                        studentservice.countStudents()
                ));
    }

}
