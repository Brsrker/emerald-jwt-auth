package com.brsrker.emerald.jwt.auth.controller;

import com.brsrker.emerald.jwt.auth.model.UserDTO;
import com.brsrker.emerald.jwt.auth.model.UserRegisterDTO;
import com.brsrker.emerald.jwt.auth.security.IsEmeraldManager;
import com.brsrker.emerald.jwt.auth.service.UserService;
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
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> register(@RequestBody @Validated UserRegisterDTO user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @IsEmeraldManager
    @GetMapping(value = "/users")
    public ResponseEntity<ListResultDTO<UserDTO>> list(
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size,
            @RequestParam(name = "sort_by") Optional<String> sortBy) {
        Pageable pageable = PageRequest.of(
                Math.max(page.orElse(1), 1) - 1,
                Math.max(size.orElse(10), 1),
                Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(userService.list(pageable));
    }

    @IsEmeraldManager
    @GetMapping(value = "/users/{login_id}")
    public ResponseEntity<UserDTO> find(@PathVariable(name = "login_id") String loginId) {
        return ResponseEntity.ok(userService.findOne(loginId));
    }

    @IsEmeraldManager
    @DeleteMapping(value = "/users/{login_id}")
    public ResponseEntity<?> delete(@PathVariable(name = "login_id") String loginId) {
        userService.deleteOne(loginId);
        return ResponseEntity.ok().build();
    }

}
