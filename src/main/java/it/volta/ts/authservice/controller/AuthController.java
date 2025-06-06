package it.volta.ts.authservice.controller;


import it.volta.ts.authservice.dto.*;
import it.volta.ts.authservice.entity.User;
import it.volta.ts.authservice.security.UserPrincipal;
import it.volta.ts.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request
    ) {
        System.out.println("Auth = " + authentication);
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return ResponseEntity.status(403).body("Unauthorized or invalid token");
        }

        System.out.println("Principal = " + principal);
        System.out.println("User = " + principal.user());

        authService.changePassword(principal.user(), request);
        return ResponseEntity.ok("Password changed successfully");
    }
    // В будущем здесь появятся:
    // - /refresh
    // - /logout
    // - /forgot-password

}