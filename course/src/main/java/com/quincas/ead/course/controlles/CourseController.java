package com.quincas.ead.course.controlles;

import com.quincas.ead.course.dtos.CourseDto;
import com.quincas.ead.course.models.CourseModel;
import com.quincas.ead.course.service.CourseService;
import com.quincas.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto){
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));

    }


    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "courseId") UUID userId){
        Optional<CourseModel> courseModelOptional = courseService.findById(userId);

        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }else{
            courseService.delete(courseModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
        }
    }


    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCouse(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid CourseDto courseDto){

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }else{

            var courseModel = courseModelOptional.get();

            courseModel.setName(courseDto.getName());
            courseModel.setDescription(courseDto.getDescription());
            courseModel.setImageUrl(courseDto.getImageUrl());
            courseModel.setCourseStatus(courseDto.getCourseStatus());
            courseModel.setCourseLevel(courseDto.getCourseLevel());
            courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
        }
    }



    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());

    }
}
