package vn.edu.hcmuaf.be.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.be.dto.ApiPaginationResponse;
import vn.edu.hcmuaf.be.dto.PaginationQuery;
import vn.edu.hcmuaf.be.dto.admin.CartAdminDTO;
import vn.edu.hcmuaf.be.dto.admin.CourseAdminDTO;
import vn.edu.hcmuaf.be.dto.course.CourseDTO;
import vn.edu.hcmuaf.be.entity.Cart;
import vn.edu.hcmuaf.be.entity.Course;
import vn.edu.hcmuaf.be.repository.CourseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/course")
public class AdminCourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiPaginationResponse<?>> getAllCourses(PaginationQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Course> pageResult = courseRepository.findAll(pageable);
        List<CourseAdminDTO> result = pageResult.getContent().stream().map(e->modelMapper.map(e, CourseAdminDTO.class)).toList();
        return new ResponseEntity<>(
                ApiPaginationResponse.builder()
                        .limit(query.getLimit())
                        .page(query.getPage())
                        .total((int) pageResult.getTotalElements())
                        .data(result)
                        .message("success")
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable UUID id, @RequestBody Course course) {
        Optional<Course> optional = courseRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Course existing = optional.get();
        existing.setName(course.getName());
        existing.setDescription(course.getDescription());
        existing.setPreview(course.getPreview());
        existing.setCurrentPrice(course.getCurrentPrice());
        return ResponseEntity.ok(courseRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID id) {
        if (!courseRepository.existsById(id)) return ResponseEntity.notFound().build();
        courseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
