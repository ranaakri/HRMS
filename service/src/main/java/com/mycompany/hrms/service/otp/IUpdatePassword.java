package com.mycompany.hrms.service.otp;

public interface IUpdatePassword {
    void generateAndSendOtp(String email);
    String verifyOtpAndGenerateToken(String email, String userOtp);
    boolean updatePassword(String email, String newPassword, String resetToken);
}
