package com.brsrker.emerald.jwt.auth.controller;

import com.brsrker.emerald.jwt.auth.model.PrivilegeDTO;
import com.brsrker.emerald.jwt.auth.security.IsEmeraldManager;
import com.brsrker.emerald.jwt.auth.service.PrivilegeService;
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
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @IsEmeraldManager
    @PostMapping(value = "/privileges")
    public ResponseEntity<PrivilegeDTO> create(@RequestBody @Validated PrivilegeDTO privilegeDTO) {
        return ResponseEntity.ok(privilegeService.create(privilegeDTO));
    }

    @IsEmeraldManager
    @GetMapping(value = "/privileges")
    public ResponseEntity<ListResultDTO<PrivilegeDTO>> list(
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size,
            @RequestParam(name = "sort_by") Optional<String> sortBy) {
        Pageable pageable = PageRequest.of(
                Math.max(page.orElse(1), 1) - 1,
                Math.max(size.orElse(10), 1),
                Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(privilegeService.list(pageable));
    }

    @IsEmeraldManager
    @GetMapping(value = "/privileges/{name}")
    public ResponseEntity<PrivilegeDTO> find(@PathVariable(name = "name") String name) {
        return ResponseEntity.ok(privilegeService.findOne(name));
    }

    @IsEmeraldManager
    @PutMapping(value = "/privileges/{name}")
    public ResponseEntity<PrivilegeDTO> update(@PathVariable(name = "name") String name, @RequestBody @Validated PrivilegeDTO privilegeDTO) {
        return ResponseEntity.ok(privilegeService.update(name, privilegeDTO));
    }

    @IsEmeraldManager
    @DeleteMapping(value = "/privileges/{name}")
    public ResponseEntity<?> delete(@PathVariable(name = "name") String name) {
        privilegeService.deleteOne(name);
        return ResponseEntity.ok().build();
    }

}
