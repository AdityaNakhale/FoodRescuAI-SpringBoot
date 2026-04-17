package org.example.project3.Controller;

import org.example.project3.Model.*;
import org.example.project3.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class AdminSearchController {

    @Autowired private FoodRepository donorRepo;
    @Autowired private NGORepository ngoRepo;
    @Autowired private DonationRepository foodRepo;

    @GetMapping("/search/donors")
    public List<DonorRegistration> searchDonors(@RequestParam String keyword) {
        return keyword.isEmpty() ? donorRepo.findAll() : donorRepo.searchDonors(keyword);
    }

    @GetMapping("/search/ngos")
    public List<NGORegistration> searchNGOs(@RequestParam String keyword) {
        return keyword.isEmpty() ? ngoRepo.findAll() : ngoRepo.searchNGOs(keyword);
    }

    @GetMapping("/search/donations")
    public List<FoodDonar> searchDonations(@RequestParam String keyword) {
        return keyword.isEmpty() ? foodRepo.findAll() : foodRepo.searchDonations(keyword);
    }
}