package bg.nbu.cscb532.logistics.data.repository;

import bg.nbu.cscb532.logistics.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
}
