package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    public Optional<User> findById(Long id);

    public List<User> findAll();
}
