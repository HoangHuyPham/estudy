package vn.edu.hcmuaf.be.controller.global;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.hcmuaf.be.dto.ApiPaginationResponse;
import vn.edu.hcmuaf.be.dto.ApiResponse;
import vn.edu.hcmuaf.be.dto.CoursePaginationQuery;
import vn.edu.hcmuaf.be.dto.course.CourseDTO;
import vn.edu.hcmuaf.be.entity.Course;
import vn.edu.hcmuaf.be.repository.CourseRepository;

@RestController
@RequestMapping("/api/global/")
public class CourseController {
    @Autowired
    private CourseRepository repoCourse;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiPaginationResponse<?>> getAll(CoursePaginationQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Course> pageResult = repoCourse.findByConditions(query.getMinPrice(), query.getMaxPrice(), query.getKeyword(), pageable);
        List<CourseDTO> result = pageResult.getContent().stream().map(e->modelMapper.map(e, CourseDTO.class)).toList();

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

    @GetMapping(value = "course/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getId(@PathVariable UUID id) {
        Course existCourse = repoCourse.findById(id).orElse(null);
        CourseDTO courseDTO = modelMapper.map(existCourse, CourseDTO.class);

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .data(courseDTO)
                        .message("success")
                        .build(),
                HttpStatus.OK);
    }
}
