package it.volta.ts.authservice.controller;


import it.volta.ts.authservice.dto.LoginRequest;
import it.volta.ts.authservice.dto.LoginResponse;
import it.volta.ts.authservice.dto.RegisterRequest;
import it.volta.ts.authservice.dto.RegisterResponse;
import it.volta.ts.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new RegisterResponse(
                "Регистрация прошла успешно. Проверьте почту для подтверждения."
        ));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email successfully verified!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    // В будущем здесь появятся:
    // - /refresh
    // - /logout
    // - /forgot-password
    // - /reset-password
}