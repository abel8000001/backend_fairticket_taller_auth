package upb.edu.co.fairticket.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upb.edu.co.fairticket.adapter.in.rest.dto.request.LoginRequest;
import upb.edu.co.fairticket.adapter.in.rest.dto.request.RegisterUserRequest;
import upb.edu.co.fairticket.adapter.in.rest.dto.response.AuthResponse;
import upb.edu.co.fairticket.domain.usecase.user.LoginUseCase;
import upb.edu.co.fairticket.domain.usecase.user.RegisterUserUseCase;
import upb.edu.co.fairticket.infrastructure.security.JwtService;

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterUserRequest request) {
        var user = switch (request.role().toUpperCase()) {
            case "ORGANIZER" -> registerUserUseCase.registerOrganizer(request.name(), request.email(), request.password());
            default -> registerUserUseCase.registerBuyer(request.name(), request.email(), request.password());
        };

        String token = jwtService.generateToken(user);
        AuthResponse response = new AuthResponse(token, user.getId().toString(), user.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        var user = loginUseCase.login(request.email(), request.password());
        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(token, user.getId().toString(), user.getRole().name());
        return ResponseEntity.ok(response);
    }
}
