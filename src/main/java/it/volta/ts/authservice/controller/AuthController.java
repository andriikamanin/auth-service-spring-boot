package it.volta.ts.authservice.controller;


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

    // В будущем здесь появятся:
    // - /login
    // - /verify
    // - /refresh
    // - /logout
    // - /forgot-password
    // - /reset-password
}