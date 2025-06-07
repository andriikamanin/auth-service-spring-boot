package it.volta.ts.authservice.controller;


import it.volta.ts.authservice.dto.*;
import it.volta.ts.authservice.entity.User;
import it.volta.ts.authservice.security.UserPrincipal;
import it.volta.ts.authservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        LoginResponse tokens = authService.login(request, httpRequest);
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


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.email());
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Password successfully reset.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }






    @PostMapping("/change-email/request-old")
    public void requestOldEmailCode(@AuthenticationPrincipal UserPrincipal principal) {
        authService.sendOldEmailConfirmation(principal.user());
    }

    @PostMapping("/change-email/verify-old")
    public void verifyOldEmailCode(@AuthenticationPrincipal UserPrincipal principal,
                                   @RequestBody EmailChangeVerifyOldDto dto) {
        authService.verifyOldEmailCode(principal.user(), dto.code());
    }

    @PostMapping("/change-email/request-new")
    public void requestNewEmailCode(@AuthenticationPrincipal UserPrincipal principal,
                                    @RequestBody EmailChangeRequestNewDto dto) {
        authService.sendNewEmailConfirmation(principal.user(), dto.newEmail());
    }

    @PostMapping("/change-email/verify-new")
    public void verifyNewEmailAndUpdate(@AuthenticationPrincipal UserPrincipal principal,
                                        @RequestBody EmailChangeVerifyNewDto dto) {
        authService.verifyNewEmailAndUpdate(principal.user(), dto.code());
    }


}