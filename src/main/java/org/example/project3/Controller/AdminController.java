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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String dashboard(HttpSession session, Model model, @RequestParam(required = false) String section) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("totalNgos", ngoRepo.count());
        model.addAttribute("totalDonations", donationRepo.count());
        model.addAttribute("ngos", ngoRepo.findAll());
        model.addAttribute("donations", donationRepo.findAll());
        model.addAttribute("donors", foodRepo.findAll());

        model.addAttribute("activeSection", (section != null) ? section : "statsSection");


        return "AdminDashboard";
    }

    @GetMapping("/load/view-donor/{id}")
    public String loadEditDonorFragment(@PathVariable Long id, Model model) {
        model.addAttribute("donor", foodRepo.findById(id).orElse(null));
        return "fragments/edit-donor :: editDonor";
    }

    @GetMapping("/load/view-ngo/{id}")
    public String loadEditNgoFragment(@PathVariable Long id, Model model) {
        model.addAttribute("ngo", ngoRepo.findById(id).orElse(null));
        return "fragments/edit-ngo :: editNgo";
    }

    @GetMapping("/deleteNgo/{id}")
    public String deleteNgo(@PathVariable Long id, HttpSession session, @RequestParam String section, RedirectAttributes redirectAttributes) {
            ngoRepo.deleteById(id);
        return "redirect:/admin/dashboard?section="+ section;
    }

    @GetMapping("/deleteDonor/{id}")
    public String deleteDonor(@PathVariable Long id, HttpSession session, @RequestParam String section, RedirectAttributes redirectAttributes) {
            foodRepo.deleteById(id);
        return "redirect:/admin/dashboard?section="+ section;
    }

    }