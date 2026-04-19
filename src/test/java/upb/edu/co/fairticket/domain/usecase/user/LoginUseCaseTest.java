package upb.edu.co.fairticket.domain.usecase.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upb.edu.co.fairticket.domain.exception.DomainException;
import upb.edu.co.fairticket.domain.model.User;
import upb.edu.co.fairticket.domain.model.valueobjects.Email;
import upb.edu.co.fairticket.domain.port.CryptoHasher;
import upb.edu.co.fairticket.domain.port.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CryptoHasher cryptoHasher;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    void shouldLoginSuccessfullyWhenCredentialsAreValid() {
        User existingUser = User.createBuyer("Shakira", new Email("shakira@barranquilla.com"), "hashed-password");
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(existingUser));
        when(cryptoHasher.matches(eq("my-secret"), eq("hashed-password"))).thenReturn(true);

        User result = loginUseCase.login("shakira@barranquilla.com", "my-secret");

        assertEquals(existingUser.getId(), result.getId());
        verify(userRepository).findByEmail(any(Email.class));
        verify(cryptoHasher).matches("my-secret", "hashed-password");
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> loginUseCase.login("ghost@nowhere.com", "123456"));
    }

    @Test
    void shouldThrowWhenPasswordDoesNotMatch() {
        User existingUser = User.createOrganizer("Juanes", new Email("juanes@medellin.com"), "hashed-password");
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(existingUser));
        when(cryptoHasher.matches(eq("wrong-password"), eq("hashed-password"))).thenReturn(false);

        assertThrows(DomainException.class, () -> loginUseCase.login("juanes@medellin.com", "wrong-password"));
    }
}
