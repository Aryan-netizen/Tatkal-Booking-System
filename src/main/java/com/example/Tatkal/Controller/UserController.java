package com.example.Tatkal.Controller;

import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/userss", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService usersService;

    public UserController(final UserService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUsers(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(usersService.getById(id));
    }

    @GetMapping("/email/{id}")
    public ResponseEntity<Users> getUsers(@PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(usersService.getByEmail(id));
    }

    @PostMapping
    public ResponseEntity<Users> createUsers(@RequestBody @Valid final Users user) {
        final Users createdId = usersService.create(user);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUsers(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final Users user) {
        usersService.update(id, user);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable(name = "id") final Long id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
