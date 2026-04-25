package org.example.project3;

import org.example.project3.Model.Feedback;
import org.example.project3.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SpringBootApplication
public class Project3Application {

    public static void main(String[] args) {
        SpringApplication.run(Project3Application.class, args);
    }


    @GetMapping("/")
    public String showLandingPage(Model model) {
        if (!model.containsAttribute("feedback")) {
            model.addAttribute("feedback", new Feedback());
        }
        return "Home";
    }


    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping("/feedback/submit")
    public String submitFeedback(@ModelAttribute("feedback") Feedback feedback,
                                 RedirectAttributes redirectAttributes) {

        feedback.setStatus("PENDING");
        feedbackRepository.save(feedback);

        System.out.println("Saved Feedback: ");

        // Add a success message to be displayed after redirect
        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your feedback has been received.");

        return "redirect:/";
    }


}
