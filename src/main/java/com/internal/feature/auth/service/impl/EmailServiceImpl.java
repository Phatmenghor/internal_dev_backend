package com.internal.feature.auth.service.impl;

import com.internal.feature.auth.service.EmailService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendOtpEmail(String toEmail, String otp, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset OTP - Internal DEV System");
            helper.setText(buildEmailContent(otp, userName), true);

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
            
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private String buildEmailContent(String otp, String userName) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f9f9f9;
                    }
                    .header {
                        background-color: #4CAF50;
                        color: white;
                        padding: 20px;
                        text-align: center;
                        border-radius: 5px 5px 0 0;
                    }
                    .content {
                        background-color: white;
                        padding: 30px;
                        border-radius: 0 0 5px 5px;
                    }
                    .otp-box {
                        background-color: #f0f0f0;
                        border: 2px dashed #4CAF50;
                        padding: 20px;
                        text-align: center;
                        margin: 20px 0;
                        border-radius: 5px;
                    }
                    .otp {
                        font-size: 32px;
                        font-weight: bold;
                        color: #4CAF50;
                        letter-spacing: 5px;
                    }
                    .warning {
                        background-color: #fff3cd;
                        border-left: 4px solid #ffc107;
                        padding: 10px;
                        margin: 20px 0;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 20px;
                        color: #777;
                        font-size: 12px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Password Reset Request</h1>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>We received a request to reset your password for your Internal DEV System account.</p>
                        
                        <p>Your One-Time Password (OTP) is:</p>
                        
                        <div class="otp-box">
                            <div class="otp">%s</div>
                        </div>
                        
                        <div class="warning">
                            <strong>⚠️ Important:</strong>
                            <ul>
                                <li>This OTP is valid for <strong>15 minutes</strong></li>
                                <li>Do not share this OTP with anyone</li>
                                <li>If you didn't request this, please ignore this email</li>
                            </ul>
                        </div>
                        
                        <p>If you have any questions or didn't request this password reset, please contact your system administrator immediately.</p>
                        
                        <p>Best regards,<br>Internal DEV System Team</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated email. Please do not reply to this message.</p>
                        <p>&copy; 2025 Internal DEV System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, userName != null ? userName : "User", otp);
    }
}