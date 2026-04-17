package org.example.project3.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.project3.Model.AllocationRequest;
import org.example.project3.Model.DonorRegistration;
import org.example.project3.Model.FoodDonar;
import org.example.project3.Model.NGORegistration;
import org.example.project3.Repository.AllocationRequestRepository;
import org.example.project3.Repository.DonationRepository;
import org.example.project3.Repository.FoodRepository;
import org.example.project3.Repository.NGORepository;
import org.example.project3.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ServiceController {

    @Autowired
    private DonationRepository donationRepo;

    @Autowired
    private NGORepository ngoRepo;

    @Autowired
    private RuleEngineService ruleService;

    @Autowired
    private AllocationRequestRepository requestRepo;






    @GetMapping("/allocateDonation/{id}")
    public String saveFood(HttpSession session, Model model,@PathVariable Long id) {
        FoodDonar food = donationRepo.findById(id).get();


        DonorRegistration user = (DonorRegistration) session.getAttribute("user");

        // 2. Load all potential partners from database
        List<NGORegistration> ngos = ngoRepo.findAll();

        // 3. AI Allocation: Filter -> Batch Distance Call -> Scoring
        NGORegistration selected = ruleService.allocateFood(food, ngos, user);

        if (selected == null) {
            model.addAttribute("result", "No NGO found with sufficient capacity or proximity.");
            return "result";
        }

        // 4. Persist the result into the donation record
        food.setAssignedNgoId(selected.getId());
        food.setStatusrequest("Assigned");
        donationRepo.save(food);

        //5. Audit Log (Persistence)
        AllocationRequest req = new AllocationRequest();
        req.setFoodId(food.getId());
        req.setNgoId(selected.getId());
        req.setStatus("ALLOCATED");
        requestRepo.save(req);

        // 6. UI Notification
        model.addAttribute("result", "Food Allocated to nearest NGO: " + selected.getNgoName());

        return "result";
    }



    @GetMapping("/register/donor")
    public String registerDonor(HttpSession session, Model model) {
        return null;
    }
    @GetMapping("/register/ngo")
    public String registerNgo(HttpSession session, Model model) {
        return null;
    }


}
