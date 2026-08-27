package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.UserCreateDTO;
import com.example.Tatkal.Dto.UsersDTO;
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
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService usersService;

    public UserController(final UserService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<UsersDTO>> getAllUsers() {
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUsers(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(usersService.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsersDTO> getUsersByEmail(@PathVariable(name = "email") final String email) {
        return ResponseEntity.ok(usersService.getByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UsersDTO> createUsers(@RequestBody @Valid final UserCreateDTO userCreateDTO) {
        final UsersDTO createdUser = usersService.create(userCreateDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUsers(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final UsersDTO usersDTO) {
        UsersDTO updatedUser = usersService.update(id, usersDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable(name = "id") final Long id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
