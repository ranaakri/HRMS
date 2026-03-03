package com.mycompany.hrms.service.otp;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.email.EmailService;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService implements IUpdatePassword {

    private final LoadingCache<String, String> otpCache;
    private final EmailService emailService;
    private final UsersRepo usersRepo;
    private final SecureRandom secureRandom;
    private final PasswordEncoder passwordEncoder;
    private final LoadingCache<String, String> resetTokenCache;

    public OtpService(EmailService emailService,
                      UsersRepo usersRepo,
                      PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
        this.otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) { return ""; }
                });

        this.resetTokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<>() { public String load(String k) { return ""; } });
        secureRandom = new SecureRandom();
    }

    public void generateAndSendOtp(String email) {
        if(!usersRepo.existsByEmail(email))
            throw new ResourceNotFoundException("User not found with this email");

        String otp = String.valueOf(100000 + secureRandom.nextInt(900000));

        otpCache.put(email, otp);

        try{
            emailService.sendEmail(email, "Your Password Reset OTP", "Your OTP is: " + otp + ". Valid for 5 minutes.");
        }catch (Exception ex){
            throw new InternalServerException("Error in sending email: " + ex);
        }
    }

    public String verifyOtpAndGenerateToken(String email, String userOtp) {
        String cachedOtp = otpCache.getIfPresent(email);

        if (cachedOtp != null && cachedOtp.equals(userOtp)) {
            otpCache.invalidate(email);

            String resetToken = UUID.randomUUID().toString();
            resetTokenCache.put(resetToken, email);
            return resetToken;
        }
        return null;
    }

    @Transactional
    public boolean updatePassword(String email, String newPassword, String userResetToken){
        Users user = usersRepo.findUsersByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String resetToken = resetTokenCache.getIfPresent(userResetToken);

        if(resetToken != null && resetToken.equals(email))
        {
            resetTokenCache.invalidate(resetToken);
            user.setPassword(passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }
}

