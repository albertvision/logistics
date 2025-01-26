package bg.nbu.cscb532.logistics.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        String defaultEncoderId = "bcrypt";
        HashMap<String, PasswordEncoder> encoders = new HashMap<>() {{
            put(defaultEncoderId, new BCryptPasswordEncoder());
        }};

        return new DelegatingPasswordEncoder(defaultEncoderId, encoders);
    }
}
