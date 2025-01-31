package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public interface AuthService extends UserDetailsManager {
    public User getLoggedInUser();
}
