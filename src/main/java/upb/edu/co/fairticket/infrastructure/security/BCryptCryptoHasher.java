package upb.edu.co.fairticket.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import upb.edu.co.fairticket.domain.port.CryptoHasher;

@Component
public class BCryptCryptoHasher implements CryptoHasher {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String rawValue) {
        return passwordEncoder.encode(rawValue);
    }

    @Override
    public boolean matches(String rawValue, String hashedValue) {
        return passwordEncoder.matches(rawValue, hashedValue);
    }
}
