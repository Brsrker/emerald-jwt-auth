package com.brsrker.emerald.jwt.auth.controller;

import com.brsrker.emerald.jwt.auth.model.RoleDTO;
import com.brsrker.emerald.jwt.auth.service.RoleService;
import com.brsrker.emerald.jwt.auth.security.IsEmeraldManager;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    @IsEmeraldManager
    @PostMapping(value = "/roles")
    public ResponseEntity<RoleDTO> create(@RequestBody @Validated RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.create(roleDTO));
    }

    @IsEmeraldManager
    @GetMapping(value = "/roles")
    public ResponseEntity<ListResultDTO<RoleDTO>> list(
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size,
            @RequestParam(name = "sort_by") Optional<String> sortBy) {
        Pageable pageable = PageRequest.of(
                Math.max(page.orElse(1), 1) - 1,
                Math.max(size.orElse(10), 1),
                Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(roleService.list(pageable));
    }

    @IsEmeraldManager
    @GetMapping(value = "/roles/{name}")
    public ResponseEntity<RoleDTO> find(@PathVariable(name = "name") String name) {
        return ResponseEntity.ok(roleService.findOne(name));
    }

    @IsEmeraldManager
    @PutMapping(value = "/roles/{name}")
    public ResponseEntity<RoleDTO> update(@PathVariable(name = "name") String name, @RequestBody @Validated RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.update(name, roleDTO));
    }

    @IsEmeraldManager
    @DeleteMapping(value = "/roles/{name}")
    public ResponseEntity<?> delete(@PathVariable(name = "name") String name) {
        roleService.deleteOne(name);
        return ResponseEntity.ok().build();
    }

}
