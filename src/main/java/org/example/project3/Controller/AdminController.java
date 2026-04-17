package org.example.project3.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.project3.Model.DonorRegistration;
import org.example.project3.Model.NGORegistration;
import org.example.project3.Repository.FoodRepository;
import org.example.project3.Repository.NGORepository;
import org.example.project3.Repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NGORepository ngoRepo;

    @Autowired
    private DonationRepository donationRepo;

    @Autowired
    private FoodRepository foodRepo;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("totalNgos", ngoRepo.count());
        model.addAttribute("totalDonations", donationRepo.count());
        model.addAttribute("ngos", ngoRepo.findAll());
        model.addAttribute("donations", donationRepo.findAll());
        model.addAttribute("donors", foodRepo.findAll());

        return "AdminDashboard";
    }

    @GetMapping("/load/edit-donor/{id}")
    public String loadEditDonorFragment(@PathVariable Long id, Model model) {
        model.addAttribute("donor", foodRepo.findById(id).orElse(null));
        return "fragments/edit-donor :: editDonor";
    }

    @GetMapping("/load/edit-ngo/{id}")
    public String loadEditNgoFragment(@PathVariable Long id, Model model) {
        model.addAttribute("ngo", ngoRepo.findById(id).orElse(null));
        return "fragments/edit-ngo :: editNgo";
    }



}