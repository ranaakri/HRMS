package com.mycompany.hrms.api.controllers.users;
import com.mycompany.hrms.api.utils.JwtUtil;
import com.mycompany.hrms.service.dtos.users.request.LoginRequest;
import com.mycompany.hrms.service.dtos.users.request.UpdatePasswordReq;
import com.mycompany.hrms.service.dtos.users.request.VerifyOtpReq;
import com.mycompany.hrms.service.dtos.users.response.AuthResponse;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.UnAuthorizedException;
import com.mycompany.hrms.service.otp.IUpdatePassword;
import com.mycompany.hrms.service.otp.OtpService;
import com.mycompany.hrms.service.users.UsersService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;
    private final IUpdatePassword updatePassword;


    private static final String JWT = "JWT_TOKEN";

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsersService usersService,
                          IUpdatePassword updatePassword){
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
        this.updatePassword = updatePassword;
    }

    @Operation(
            summary = "Login using credentials"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        if(usersService.isBlocked(loginRequest.getEmail()))
            throw new UnAuthorizedException("User is blocked");
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if(userDetails == null)
            throw new InternalServerException("Error getting details for jwt");
        String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());
        String refreshToken  = jwtUtil.generateRefreshToken(auth);
        ResponseCookie cookie = ResponseCookie.from(JWT, token)
                .httpOnly(true)
                .path("/")
                .secure(false)
                .maxAge(60 * (long)60)
                .sameSite("Lax")
                .build();

        ResponseCookie springCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * (long) 24)
                .sameSite("Lax")
                .build();

        Cookie cookie1 = new Cookie("LoggedIn", "login");
        cookie1.setPath("/");
        cookie1.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie1);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, springCookie.toString());

        return ResponseEntity.ok(usersService.getUserRole(loginRequest.getEmail()));
    }

    @Operation(
            summary = "Generate JWT using refresh token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response){

        Claims claims = jwtUtil.getClaims(refreshToken);
        if(!"refresh".equals(claims.get("type"))){
            throw new BadRequestException("Invalid token type");
        }
        String userName = claims.getSubject();
        String role = claims.get("role", String.class);
        String newAccessToken = jwtUtil.generateToken(userName, role);

        ResponseCookie token = ResponseCookie.from(JWT, newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * (long)60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, token.toString());
        return ResponseEntity.ok("refresh success");
    }

    @Operation(
            summary = "Logout",
            description = "Removes all login related cookies form browser"
    )
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){

        ResponseCookie cookie = ResponseCookie.from(JWT, "")
                .httpOnly(true)
                .path("/")
                .secure(false)
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie springCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        Cookie cookie1 = new Cookie("LoggedIn", "login");
        cookie1.setPath("/");
        cookie1.setMaxAge(0);

        response.addCookie(cookie1);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, springCookie.toString());

        return ResponseEntity.ok("Log out successful");
    }

    @Operation(
            summary = "Generate Opp",
            description = "Generate otp by email"
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        updatePassword.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @Operation(
            summary = "Verify OTP"
    )
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpReq req){
        String resetToken = updatePassword.verifyOtpAndGenerateToken(req.getEmail(), req.getOtp());
        if(resetToken!=null)
            return new ResponseEntity<>(resetToken, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
            summary = "Reset password"
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordReq req) {
        if (updatePassword.updatePassword(req.getEmail(),req.getPassword(), req.getResetToken())) {
            return ResponseEntity.ok("Password Updated");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token.");
    }
}
