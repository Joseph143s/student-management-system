package Mangement.StudentManagement.Controller;


import Mangement.StudentManagement.DTO.Request.FacultyRequestDTO;
import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.DTO.Response.FacultyResponseDTO;
import Mangement.StudentManagement.Service.FacultyService;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculties")
@Tag(
        name="Faculty APIs",
        description = "Operations related to Faculties"

)
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService){
         this.facultyService=facultyService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "save the details of the Faculty")
    @PostMapping
    public ResponseEntity<ApiResponse<FacultyResponseDTO>> saveFaculty(@Valid @RequestBody FacultyRequestDTO dto){

        FacultyResponseDTO response=facultyService.saveFaculty(dto);
        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                         201,
                        "Faculty saved successfully",
                        response
                ), HttpStatus.CREATED
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="Get all Faculties")
    @GetMapping

    public ResponseEntity<ApiResponse<List<FacultyResponseDTO>>> getALlFaculties(){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Faculties fetched successfully",
                        facultyService.getAllFaculties()
                )
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="Get faculty by their Id")

    @GetMapping("/{facultyId}")

    public ResponseEntity<ApiResponse<FacultyResponseDTO>> getFacultyById(@PathVariable int facultyId){
          FacultyResponseDTO response=facultyService.getFacultyById(facultyId);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Faculty fetched successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation (summary=" update the faculty ...if the faculty id present")

    @PutMapping("/{facultyId}")

    public ResponseEntity<ApiResponse<FacultyResponseDTO>> updateFaculties(@PathVariable int facultyId, @RequestBody FacultyRequestDTO dto){
        FacultyResponseDTO response=facultyService.updateFaculty(facultyId,dto);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Faculty updated successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="Delete the faculty.... who exists and we didn't want")

    @DeleteMapping("/{facultyId}")

    public ResponseEntity<ApiResponse<String>>deleteFacultyById(@PathVariable int facultyId){
        facultyService.deleteFaculty(facultyId);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Faculty deleted suceesfully",
                        null
                )
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="Existence of Faculty checking")

    @GetMapping("/exists/{facultyId}")
    public ResponseEntity<ApiResponse<Boolean>> existsById(@PathVariable int facultyId){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Existence checked successfully",
                        facultyService.existsById(facultyId)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")

    @Operation(summary="count the total number of faculties are there on entire all departments")

    @GetMapping("/count")

    public ResponseEntity<ApiResponse<Long>>countFaculty(){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                200,
                "Faculty counted successfully",
                facultyService.countFaculities()
        )
        );
    }
}
