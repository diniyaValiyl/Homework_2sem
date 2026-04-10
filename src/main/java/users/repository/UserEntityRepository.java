package users.repository;

import org.springframework.stereotype.Repository;
import users.model.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class UserEntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    public Optional<UserEntity> findById(Long id) {
        UserEntity user = entityManager.find(UserEntity.class, id);
        return Optional.ofNullable(user);
    }

    public List<UserEntity> findAll() {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "SELECT u FROM UserEntity u", UserEntity.class);
        return query.getResultList();
    }

    public List<UserEntity> findOlderThan(LocalDate date) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "SELECT u FROM UserEntity u WHERE u.birthDate < :date", UserEntity.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Transactional
    public boolean delete(Long id) {
        UserEntity user = entityManager.find(UserEntity.class, id);
        if (user != null) {
            entityManager.remove(user);
            return true;
        }
        return false;
    }
}