package users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.config.UserStatus;
import users.model.UserEntity;
import users.service.UserEntityService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jpa/users")
public class UserEntityController {

    @Autowired
    private UserEntityService service;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserEntity> create(
            @RequestParam String name,
            @RequestParam(required = false) LocalDate birthDate) {
        if (birthDate == null) {
            birthDate = LocalDate.now();
        }
        UserEntity user = service.create(name, birthDate);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/with-post")
    public ResponseEntity<UserEntity> createWithPost(
            @RequestParam String name,
            @RequestParam String postTitle,
            @RequestParam String postContent) {
        UserEntity user = service.createWithPost(name, LocalDate.now(), postTitle, postContent);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserEntity> updateStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        try {
            return ResponseEntity.ok(service.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/older-than")
    public ResponseEntity<List<UserEntity>> getOlderThan(@RequestParam LocalDate date) {
        return ResponseEntity.ok(service.findOlderThan(date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}