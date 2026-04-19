package upb.edu.co.fairticket.domain.usecase.user;

import lombok.RequiredArgsConstructor;
import upb.edu.co.fairticket.domain.model.User;
import upb.edu.co.fairticket.domain.model.valueobjects.Email;
import upb.edu.co.fairticket.domain.port.UserRepository;
import upb.edu.co.fairticket.domain.exception.DomainException;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public User registerBuyer(String name, String emailStr) {
        Email email = new Email(emailStr);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DomainException("Email already registered: " + emailStr);
        }
        User user = User.createBuyer(name, email);
        return userRepository.save(user);
    }
    
    public User registerOrganizer(String name, String emailStr) {
        Email email = new Email(emailStr);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DomainException("Email already registered: " + emailStr);
        }
        User user = User.createOrganizer(name, email);
        return userRepository.save(user);
    }
}
