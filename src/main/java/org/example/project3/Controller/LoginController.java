package org.example.project3.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.project3.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class    LoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "Login"; // Combined login page for all
    }

    @PostMapping("/auth/loginuser")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String role,
                               HttpSession session,
                               Model model) {

        System.out.println("EMAIL: " + email);
        System.out.println("PASSWORD: " + password);
        System.out.println("ROLE: " + role);

        if ("ADMIN".equals(role)) {
            if (authService.loginAdmin(email, password, session)) {
                return "redirect:/admin/dashboard";
            }
        } else {
            Object user = authService.login(email, password, role, session);
            if (user != null) {
                role = (String) session.getAttribute("userRole");
                if ("NGO".equals(role)) {
                    return "redirect:/dashboard/ngo";
                }
                return "redirect:/dashboard/user";
            }
        }

        // 🔥 ADD THIS LINE
        model.addAttribute("role", role);

        model.addAttribute("error", "Invalid Credentials");
        return "Login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/login?logout=true";
    }
}