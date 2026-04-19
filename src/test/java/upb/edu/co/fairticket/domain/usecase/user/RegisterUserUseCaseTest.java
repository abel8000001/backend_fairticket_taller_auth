package upb.edu.co.fairticket.domain.usecase.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CryptoHasher cryptoHasher;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @ParameterizedTest
    @CsvSource({
            "Carlos Vives, carlos.vives@santamarta.com, buyer-pass, hash-buyer",
            "Shakira, shakira@barranquilla.com, shaki-pass, hash-shaki",
            "Maluma, maluma@medellin.com, maluma-pass, hash-maluma"
    })
    void testRegisterMultipleBuyersSuccess(String name, String emailStr, String password, String expectedHash) {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        when(cryptoHasher.hash(eq(password))).thenReturn(expectedHash);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = registerUserUseCase.registerBuyer(name, emailStr, password);

        assertNotNull(result);
        assertTrue(result.isBuyer());
        assertEquals(name, result.getName());
        assertEquals(emailStr, result.getEmail().value());
        assertEquals(expectedHash, result.getPasswordHash());
        verify(cryptoHasher).hash(password);
        verify(userRepository).save(any(User.class));
    }

    @ParameterizedTest
    @CsvSource({
            "J Balvin, jose@vibras.com, organizer-pass, hash-organizer",
            "Julio Correal, julio@rockalparque.com, rock-pass, hash-rock"
    })
    void testRegisterMultipleOrganizersSuccess(String name, String emailStr, String password, String expectedHash) {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        when(cryptoHasher.hash(eq(password))).thenReturn(expectedHash);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = registerUserUseCase.registerOrganizer(name, emailStr, password);

        assertNotNull(result);
        assertTrue(result.isOrganizer());
        assertEquals(name, result.getName());
        assertEquals(expectedHash, result.getPasswordHash());
    }

    @Test
    void testRegisterBuyerDuplicateEmailThrowsException() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(
                Optional.of(User.createBuyer("Existing", new Email("falcao@santamarta.com"), "hash-existing"))
        );

        DomainException exception = assertThrows(DomainException.class,
                () -> registerUserUseCase.registerBuyer("Radamel Falcao", "falcao@santamarta.com", "some-pass"));

        assertTrue(exception.getMessage().contains("Email already registered"));
        verify(cryptoHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterOrganizerDuplicateEmailThrowsException() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(
                Optional.of(User.createBuyer("Existing", new Email("goyo@chocquibtown.com"), "hash-existing"))
        );

        assertThrows(DomainException.class,
                () -> registerUserUseCase.registerOrganizer("Goyo", "goyo@chocquibtown.com", "some-pass"));

        verify(cryptoHasher, never()).hash(any());
    }
}
