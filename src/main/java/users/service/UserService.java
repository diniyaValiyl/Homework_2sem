package users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import users.config.UserStatus;
import users.model.User;
import users.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User create(String name) {
        return create(name, LocalDate.now(), UserStatus.REGISTERED);
    }

    public User create(String name, LocalDate birthDate) {
        return create(name, birthDate, UserStatus.REGISTERED);
    }

    public User create(String name, LocalDate birthDate, UserStatus status) {
        try {
            User user = User.builder()
                    .name(name)
                    .birthDate(birthDate)
                    .status(status != null ? status : UserStatus.REGISTERED)
                    .build();
            return repository.save(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    public List<User> getAll() {
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to fetch users: " + e.getMessage(), e);
        }
    }

    public User getById(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("User not found with id: " + id, e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to fetch user: " + e.getMessage(), e);
        }
    }

    public User update(Long id, String name) {
        return update(id, name, null, null);
    }

    public User update(Long id, String name, LocalDate birthDate, UserStatus status) {
        try {
            User user = getById(id);
            if (name != null) {
                user.setName(name);
            }
            if (birthDate != null) {
                user.setBirthDate(birthDate);
            }
            if (status != null) {
                user.setStatus(status);
            }
            return repository.update(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public User verifyUser(Long id) {
        return update(id, null, null, UserStatus.VERIFIED);
    }

    public boolean delete(Long id) {
        try {
            return repository.delete(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    public void deleteAll() {
        try {
            repository.deleteAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete all users: " + e.getMessage(), e);
        }
    }
}