package org.wa.user.service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa.auth.lib.exception.UserAuthException;
import org.wa.auth.lib.model.jwt.JwtRequest;
import org.wa.auth.lib.model.jwt.JwtResponse;
import org.wa.auth.lib.model.jwt.RefreshJwtRequest;
import org.wa.auth.lib.service.AuthService;


@RestController
@RequestMapping("v1/users/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) throws UserAuthException {
        final JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> accessToken(@RequestBody RefreshJwtRequest request) throws UserAuthException {
        final JwtResponse response = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getRefreshToken(@RequestBody RefreshJwtRequest request) throws UserAuthException {
        final JwtResponse token = authService.getRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
