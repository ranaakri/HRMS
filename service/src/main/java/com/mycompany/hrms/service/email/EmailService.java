package com.mycompany.hrms.service.email;

import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.service.exception.InternalServerException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(sender));
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public void sendEmailMultiTos(List<String> toList, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(sender));

        InternetAddress[] addresses = new InternetAddress[toList.size()];
        for (int i = 0; i < toList.size(); i++) {
            addresses[i] = new InternetAddress(toList.get(i));
        }

        message.setRecipients(MimeMessage.RecipientType.TO, addresses);
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");

        mailSender.send(message);
    }


    public void shareJob(String shareTo, Jobs job) {
        String subject = "Job Opening";
        String body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        .container { font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; }\n" +
                "        .header { background-color: #007bff; color: white; padding: 20px; text-align: center; }\n" +
                "        .content { padding: 20px; color: #333; line-height: 1.6; }\n" +
                "        .field { margin-bottom: 15px; border-bottom: 1px dashed #eee; padding-bottom: 10px; }\n" +
                "        .label { font-weight: bold; color: #555; display: block; font-size: 12px; text-transform: uppercase; }\n" +
                "        .value { font-size: 16px; color: #000; }\n" +
                "        .image-box { text-align: center; margin-top: 20px; }\n" +
                "        .image-box img { max-width: 100%; border-radius: 4px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h2>Job Opening</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"field\">\n" +
                "                <span class=\"label\">Job Title</span>\n" +
                "                <div class=\"value\">"+ job.getTitle() +"</div>\n" +
                "            </div>\n" +
                "            <div class=\"field\">\n" +
                "                <span class=\"label\">Job Type</span>\n" +
                "                <div class=\"value\">"+ job.getJobPost()+"</div>\n" +
                "            </div>\n" +
                "            <div class=\"field\">\n" +
                "                <span class=\"label\">Summary</span>\n" +
                "                <div class=\"value\">"+ job.getSummary() +"</div>\n" +
                "            </div>\n" +
                "            <div class=\"field\">\n" +
                "                <span class=\"label\">Created Date</span>\n" +
                "                <div class=\"value\">"+ job.getCreatedAt().format(DateTimeFormatter.ofPattern("DD-MM-YYYY")) +"</div>\n" +
                "            </div>\n" +
                "            <div class=\"field\">\n" +
                "                <span class=\"label\">Deadline</span>\n" +
                "                <div class=\"value\">"+ job.getLastApplicationDate().format(DateTimeFormatter.ofPattern("DD-MM-YYYY"))+"</div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div class=\"image-box\">\n" +
                "                <span class=\"label\">Job description: "+ job.getJdFilePath() +"</span><br>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        try{
            sendEmail(shareTo, subject, body);
        }catch (Exception ex){
            throw new InternalServerException("Error in sending email");
        }
    }

    public void sendWarningEmail(Users warnedUser, String reason, String postTitle) {

        String subject = "Content Warning Notice";

        String body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        .container { font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; }\n" +
                "        .header { background-color: #dc3545; color: white; padding: 20px; text-align: center; }\n" +
                "        .content { padding: 20px; color: #333; line-height: 1.6; }\n" +
                "        .field { margin-bottom: 15px; }\n" +
                "        .label { font-weight: bold; color: #555; font-size: 12px; text-transform: uppercase; }\n" +
                "        .value { font-size: 15px; margin-top: 5px; }\n" +
                "        .footer { background: #f8f9fa; padding: 15px; text-align: center; font-size: 12px; color: #777; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h2>⚠ Content Removed Due to Policy Violation</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Hello <b>" + warnedUser.getName() + "</b>,</p>\n" +
                "            <p>Your post has been removed by HR due to inappropriate content.</p>\n" +
                "            <div class=\"field\">\n" +
                "                <div class=\"label\">Post Title</div>\n" +
                "                <div class=\"value\">" + postTitle + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"field\">\n" +
                "                <div class=\"label\">Reason</div>\n" +
                "                <div class=\"value\">" + reason + "</div>\n" +
                "            </div>\n" +
                "            <p>Please ensure that future posts comply with company policies.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            This is an automated notification from HR.\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        try {
            sendEmail(warnedUser.getEmail(), subject, body);
        } catch (Exception ex) {
            throw new InternalServerException("Error sending warning email");
        }
    }

    public void sendGameSlotRejectedEmail(List<Users> users) {

        String subject = "Game Slot Request Update";

        String body = "<html>" +
                "<body>" +
                "<p>Hi ,</p>" +
                "<p>Your requested game slot was not approved.</p>" +
                "<p>You may try booking another available slot.</p>" +
                "<br>" +
                "<p>Thank you.</p>" +
                "</body>" +
                "</html>";

        try {
            sendEmailMultiTos(users.stream().map(Users::getEmail).toList(), subject, body);
        } catch (Exception ex) {
            throw new InternalServerException("Error sending rejection email");
        }
    }

    public void sendGameSlotConfirmedEmail(List<Users> users) {

        String subject = "Game Slot Confirmed";

        String body = "<html>" +
                "<body>" +
                "<p>Hi ,</p>" +
                "<p>Your game slot has been confirmed successfully.</p>" +
                "<p>Please be available at the scheduled time.</p>" +
                "<br>" +
                "<p>Thank you.</p>" +
                "</body>" +
                "</html>";
        try {
            sendEmailMultiTos(users.stream().map(Users::getEmail).toList(), subject, body);
        } catch (Exception ex) {
            throw new InternalServerException("Error sending confirmation email");
        }
    }

    public void sendGameSlotCancelledEmail(List<Users> users) {

        String subject = "Game Slot Cancelled";

        String body = "<html>" +
                "<body>" +
                "<p>Hi,</p>" +
                "<p>Your game slot has been cancelled.</p>" +
                "<p>Your request for game slot cancellation is completed.</p>" +
                "<br>" +
                "<p>Thank you.</p>" +
                "</body>" +
                "</html>";

        try {
            sendEmailMultiTos(users.stream().map(Users::getEmail).toList(), subject, body);
        } catch (Exception ex) {
            throw new InternalServerException("Error sending cancellation email");
        }
    }
}
