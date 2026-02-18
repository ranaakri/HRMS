package com.mycompany.hrms.api.controllers.users;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.api.utils.JwtUtil;
import com.mycompany.hrms.service.dtos.users.request.LoginRequest;
import com.mycompany.hrms.service.dtos.users.response.AuthResponse;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.users.UsersService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsersService usersService){
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());
        String refreshToken  = jwtUtil.generateRefreshToken(auth);
        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(60 * 60)
                .build();

        ResponseCookie springCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24)
                .sameSite("Strict")
                .build();

        Cookie cookie1 = new Cookie("LoggedIn", "login");
        cookie1.setPath("/");
        cookie1.setMaxAge(60 * 60);

        response.addCookie(cookie1);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, springCookie.toString());

        return ResponseEntity.ok(usersService.getUserRole(loginRequest.getEmail()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response){

        Claims claims = jwtUtil.getClaims(refreshToken);
        if(!"refresh".equals(claims.get("type"))){
            throw new BadRequestException("Invalid token type");
        }
        String userName = claims.getSubject();
        String role = claims.get("role", String.class);
        String newAccessToken = jwtUtil.generateToken(userName, role);

        ResponseCookie token = ResponseCookie.from("JWT_TOKEN", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, token.toString());
        return ResponseEntity.ok("refresh success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return ResponseEntity.ok("logout successful");
    }
}
