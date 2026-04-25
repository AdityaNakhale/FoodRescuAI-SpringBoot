package org.example.project3.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM_EMAIL = "your-app-email@gmail.com";

    /**
     * General method to send a simple text email.
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    /**
     * Helper method to send HTML emails (required for buttons/styling).
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(FROM_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send HTML email to " + to + ": " + e.getMessage());
        }
    }

    /**
     * Styled Donor Registration Email
     */
    public void sendDonorRegistrationEmail(String email, String name) {
        String subject = "Welcome to Food Rescue Project!";
        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #eee; padding: 20px; border-radius: 10px;'>" +
                "<div style='text-align: center; border-bottom: 2px solid #28a745; padding-bottom: 10px;'>" +
                "   <h1 style='color: #28a745; margin: 0;'>Food Rescue</h1>" +
                "</div>" +
                "<h2 style='color: #333;'>Welcome, " + name + "!</h2>" +
                "<p>Thank you for joining our mission to fight food waste. Your registration as a <strong>Donor</strong> was successful.</p>" +
                "<p>You can now start posting food donations to help those in need. Every meal saved makes a difference!</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "   <a href='http://localhost:8080/login' style='background-color: #28a745; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Go to Dashboard</a>" +
                "</div>" +
                "<p style='color: #777; font-size: 14px;'>If you did not create an account, please ignore this email.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee;'>" +
                "<p style='font-size: 12px; color: #aaa; text-align: center;'>&copy; 2024 Food Rescue Project. All rights reserved.</p>" +
                "</div>";

        sendHtmlEmail(email, subject, htmlContent);
    }

    /**
     * Styled NGO Registration Email
     */
    public void sendNGORegistrationEmail(String email, String ngoName) {
        String subject = "NGO Registration Successful";
        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #eee; padding: 20px; border-radius: 10px;'>" +
                "<div style='text-align: center; border-bottom: 2px solid #007bff; padding-bottom: 10px;'>" +
                "   <h1 style='color: #007bff; margin: 0;'>Food Rescue</h1>" +
                "</div>" +
                "<h2 style='color: #333;'>Hello " + ngoName + " Team,</h2>" +
                "<p>Your NGO has been successfully registered on our platform. We are excited to have you as a partner in food distribution.</p>" +
                "<p>You can now access the marketplace to view available donations and claim food for your community.</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "   <a href='http://localhost:8080/login' style='background-color: #007bff; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>View Marketplace</a>" +
                "</div>" +
                "<p style='color: #777; font-size: 14px;'>Welcome aboard!</p>" +
                "<hr style='border: none; border-top: 1px solid #eee;'>" +
                "<p style='font-size: 12px; color: #aaa; text-align: center;'>&copy; 2024 Food Rescue Project. All rights reserved.</p>" +
                "</div>";

        sendHtmlEmail(email, subject, htmlContent);
    }

    /**
     * Sends an allocation email with a styled button linking to Google Maps using coordinates.
     */
    public void sendAllocationEmail(String ngoEmail, String ngoName, String foodItem, String donorName, String address, Double lat, Double lng) {
        String mapUrl = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng;
        String subject = "New Food Allocation Assigned!";

        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #eee; padding: 20px; border-radius: 10px;'>" +
                "<div style='text-align: center; border-bottom: 2px solid #ffc107; padding-bottom: 10px;'>" +
                "   <h1 style='color: #333; margin: 0;'>New Allocation</h1>" +
                "</div>" +
                "<p>Dear <strong>" + ngoName + "</strong>,</p>" +
                "<p>A new food donation (<strong>" + foodItem + "</strong>) from <strong>" + donorName + "</strong> has been allocated to you.</p>" +
                "<p style='background-color: #f8f9fa; padding: 10px; border-left: 4px solid #ffc107;'>" +
                "   <strong>Pickup Address:</strong><br>" + address +
                "</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "   <a href='" + mapUrl + "' style='background-color: #28a745; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>" +
                "       View Location" +
                "   </a>" +
                "</div>" +
                "<p>Please log in to your dashboard to view further details and coordinate the pickup.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee;'>" +
                "<p style='font-size: 12px; color: #aaa; text-align: center;'>Best Regards,<br><strong>Food Rescue Team</strong></p>" +
                "</div>";

        sendHtmlEmail(ngoEmail, subject, htmlContent);
    }
}