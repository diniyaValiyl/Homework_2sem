package users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import users.model.User;
import users.repository.UserRepository;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User create(String name) {
        return repository.save(new User(null, name));
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id);
    }

    public User update(Long id, String name) {
        User user = repository.findById(id);
        if (user != null) {
            user.setName(name);
            return repository.update(user);
        }
        return null;
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}