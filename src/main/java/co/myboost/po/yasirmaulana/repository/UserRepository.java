package co.myboost.po.yasirmaulana.repository;

import co.myboost.po.yasirmaulana.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int userId);
    Page<User> findByFirstNameLikeIgnoreCase(String firstName, Pageable pageable);
}
