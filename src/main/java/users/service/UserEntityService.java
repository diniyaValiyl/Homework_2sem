package users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import users.config.UserStatus;
import users.model.PostEntity;
import users.model.UserEntity;
import users.repository.UserEntityRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository repository;

    public UserEntity create(String name, LocalDate birthDate) {
        UserEntity user = UserEntity.builder()
                .name(name)
                .birthDate(birthDate)
                .status(UserStatus.REGISTERED)
                .build();
        return repository.save(user);
    }

    @Transactional
    public UserEntity createWithPost(String name, LocalDate birthDate, String postTitle, String postContent) {
        UserEntity user = create(name, birthDate);

        PostEntity post = PostEntity.builder()
                .title(postTitle)
                .content(postContent)
                .user(user)
                .build();

        user.addPost(post);
        return repository.save(user);
    }

    public List<UserEntity> getAll() {
        return repository.findAll();
    }

    public UserEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public UserEntity updateStatus(Long id, UserStatus newStatus) {
        UserEntity user = getById(id);
        user.setStatus(newStatus);
        return repository.save(user);
    }

    public List<UserEntity> findOlderThan(LocalDate date) {
        return repository.findOlderThan(date);
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.delete(id);
    }
}