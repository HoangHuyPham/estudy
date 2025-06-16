package vn.edu.hcmuaf.be.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.hcmuaf.be.dto.ApiPaginationResponse;
import vn.edu.hcmuaf.be.dto.PaginationQuery;
import vn.edu.hcmuaf.be.dto.course.CourseDTO;
import vn.edu.hcmuaf.be.entity.Course;
import vn.edu.hcmuaf.be.repository.CourseRepository;

@RestController
@RequestMapping("/api/course/")
public class CourseController {
    @Autowired
    private CourseRepository repoCourse;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiPaginationResponse<?>> getAll(PaginationQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Course> pageResult = repoCourse.findAll(pageable);
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
}
