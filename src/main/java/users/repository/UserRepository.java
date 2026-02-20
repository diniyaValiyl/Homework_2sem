package users.repository;

import org.springframework.stereotype.Repository;
import users.model.User;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private AtomicLong idGen = new AtomicLong(1);

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGen.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User update(User user) {
        return users.replace(user.getId(), user);
    }

    public boolean delete(Long id) {
        return users.remove(id) != null;
    }
}