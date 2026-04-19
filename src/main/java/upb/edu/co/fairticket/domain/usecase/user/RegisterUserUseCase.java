package upb.edu.co.fairticket.domain.usecase.user;

import lombok.RequiredArgsConstructor;
import upb.edu.co.fairticket.domain.exception.DomainException;
import upb.edu.co.fairticket.domain.model.User;
import upb.edu.co.fairticket.domain.model.valueobjects.Email;
import upb.edu.co.fairticket.domain.port.CryptoHasher;
import upb.edu.co.fairticket.domain.port.UserRepository;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final CryptoHasher cryptoHasher;

    public User registerBuyer(String name, String emailStr, String password) {
        Email email = new Email(emailStr);
        validateEmailNotRegistered(email, emailStr);

        String passwordHash = cryptoHasher.hash(password);
        User user = User.createBuyer(name, email, passwordHash);
        return userRepository.save(user);
    }

    public User registerOrganizer(String name, String emailStr, String password) {
        Email email = new Email(emailStr);
        validateEmailNotRegistered(email, emailStr);

        String passwordHash = cryptoHasher.hash(password);
        User user = User.createOrganizer(name, email, passwordHash);
        return userRepository.save(user);
    }

    private void validateEmailNotRegistered(Email email, String emailStr) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DomainException("Email already registered: " + emailStr);
        }
    }
}
