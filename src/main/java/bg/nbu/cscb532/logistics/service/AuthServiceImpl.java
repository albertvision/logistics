package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.entity.User;
import bg.nbu.cscb532.logistics.data.enumeration.Authority;
import bg.nbu.cscb532.logistics.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void deleteUser(String username) {
        try {
            User user = (User) loadUserByUsername(username);
            userRepository.delete(user);
        } catch (UsernameNotFoundException ignored) {

        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // todo
    }

    @Override
    public boolean userExists(String username) {
        try {
            loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @EventListener
    public void seedUsers(ContextRefreshedEvent event) {
        try {
            Arrays.stream(Authority.values())
                    .map(it -> {
                        String roleString = it.name().toLowerCase();

                        return User.builder()
                                .username(roleString)
                                .name("User " + roleString)
                                .password(passwordEncoder.encode(roleString))
                                .authority(it)
                                .build();
                    })
                    .forEach(this::createUser);
        } catch (DataIntegrityViolationException e) {
            // nothing to do as users already exist
        }
    }

    @Override
    public User getLoggedInUser() {
        //todo
        return null;
    }
}
