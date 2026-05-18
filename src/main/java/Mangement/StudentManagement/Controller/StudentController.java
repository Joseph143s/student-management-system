package Mangement.StudentManagement.Controller;
import Mangement.StudentManagement.Entity.*;
import Mangement.StudentManagement.Service.*;
import Mangement.StudentManagement.ServiceImpl.*;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<StudentResponseDTO> saveStudent(@Valid @RequestBody StudentRequestDTO dto){
        StudentResponseDTO response= studentservice.saveStudent(dto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @Operation(summary=" get All students")
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(){
        return ResponseEntity.ok(studentservice.getAllStudents());
    }

    @Operation(summary="get Student by id")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable int id){

        return ResponseEntity.ok(studentservice.getStudentById(id));
    }
    @Operation(summary="get student address by student id")
    @GetMapping("/{id}/address")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable int id){

        return ResponseEntity.ok(studentservice.getStudentAddressById(id));
    }
    @Operation(summary="update student")
    @PutMapping("/{id}")
     public ResponseEntity<StudentResponseDTO>updateStudent(
             @PathVariable int id ,
             @Valid @RequestBody StudentRequestDTO dto){
        return ResponseEntity.ok(studentservice.updateStudent(id,dto));

    }

    @Operation(summary="delete student")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id){
        studentservice.deleteStudentById(id);
        return ResponseEntity.ok("Student deleted Successfully");
    }
    // CHECK STUDENT EXISTS
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable int id) {

        return ResponseEntity.ok(studentservice.existsById(id));
    }


    // COUNT TOTAL STUDENTS
    @GetMapping("/count")
    public ResponseEntity<Long> countStudents() {

        return ResponseEntity.ok(studentservice.countStudents());
    }

}
