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
import vn.edu.hcmuaf.be.dto.admin.CourseAdminDTO;
import vn.edu.hcmuaf.be.dto.admin.RoleAdminDTO;
import vn.edu.hcmuaf.be.entity.Role;
import vn.edu.hcmuaf.be.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/role")
public class AdminRoleController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiPaginationResponse<?>> getAllCourses(PaginationQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Role> pageResult = roleRepository.findAll(pageable);
        List<RoleAdminDTO> result = pageResult.getContent().stream().map(e->modelMapper.map(e, RoleAdminDTO.class)).toList();
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
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable UUID id, @RequestBody Role role) {
        Optional<Role> optional = roleRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Role existing = optional.get();
        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        return ResponseEntity.ok(roleRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable UUID id) {
        if (!roleRepository.existsById(id)) return ResponseEntity.notFound().build();
        roleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}