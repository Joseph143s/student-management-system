package Mangement.StudentManagement.Controller;

import Mangement.StudentManagement.Service.*;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import Mangement.StudentManagement.config.SecurityConfig;
import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.DTO.Request.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/departments")

@Tag(
         name="Department APTs",
        description = "Operations related to Department"
)
public class DepartmentController {

     private final DepartmentService departmentService;

     public DepartmentController(DepartmentService departmentService){

         this.departmentService=departmentService;
     }

      @PreAuthorize("hasRole('ADMIN')")
     @PostMapping
     public ResponseEntity<ApiResponse<DepartmentResponseDTO>> saveDepartment(
                  @Valid @RequestBody DepartmentRequestDTO dto){

          DepartmentResponseDTO response=departmentService.saveDepartment(dto);

          return new ResponseEntity<>(
                  ApiResponseBuilder.success(
                          201,
                          "Department saved Successfully",
                          response
                  ), HttpStatus.CREATED
                  );

     }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>>getAllDepartments(){

        return ResponseEntity.ok(
                  ApiResponseBuilder.success(
                          200,
                          "Department fetched successfully",
                          departmentService.getAllDepartments()
                  )
        );
     }


    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @Operation(summary="Get department By Id")
     @GetMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById( @PathVariable int  departmentId){

         return ResponseEntity.ok(
                   ApiResponseBuilder.success(
                            200,
                            "Department fetched successfully",
                           departmentService.getDepartmentById(departmentId)
                   )
         );

     }

    @PreAuthorize("hasRole('ADMIN')")
     @Operation(summary = "update the department by Id")

     @PutMapping("/{Id}")
      public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
               @PathVariable int Id,@Valid @RequestBody DepartmentRequestDTO dto){
          DepartmentResponseDTO updated=departmentService.updateDepartment(Id,dto);
          return ResponseEntity.ok(
                  ApiResponseBuilder.success(
                            200,
                          "Department updated successfully",
                          updated
                  )
          );
     }


    @PreAuthorize("hasRole('ADMIN')")

     @Operation(summary="Delete department")

    @DeleteMapping("/{Id}")

     public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable int Id){

         departmentService.deleteDepartmentById(Id);
         return ResponseEntity.ok(
                   ApiResponseBuilder.success(
                           200,
                           "Department deleted successfully",
                           null
                   )
         );
     }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
     @Operation(summary = "Existence Checking")

     @GetMapping("/exists/{Id}")
         public ResponseEntity<ApiResponse<Boolean>> existsBYId(@PathVariable int Id){
         return ResponseEntity.ok(
                 ApiResponseBuilder.success(
                            200,
                         "Existence checked Successfully",
                         departmentService.existsById(Id)
                 )
         );
     }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")

     @Operation(summary = "Count the existence departments")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countDepartments(){

         return ResponseEntity.ok(
                 ApiResponseBuilder.success(
                          200,
                         "count fetched successfully",
                         departmentService.countDepartments()
                 )
         );
     }

}


