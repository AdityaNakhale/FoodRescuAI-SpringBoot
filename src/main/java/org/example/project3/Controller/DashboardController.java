package org.example.project3.Controller;


import org.example.project3.Model.AllocationRequest;
import org.example.project3.Model.DonorRegistration;
import org.example.project3.Model.FoodDonar;
import org.example.project3.Model.NGORegistration;
import org.example.project3.Repository.AllocationRequestRepository;
import org.example.project3.Repository.DonationRepository;
import org.example.project3.Repository.FoodRepository;
import org.example.project3.Repository.NGORepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

        private static String UPLOAD_DIR = "food-photo/";

    @Autowired
    FoodRepository userRepository;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    NGORepository ngoRepository;

    @Autowired
    private AllocationRequestRepository allocationRepo;

    @GetMapping("/user")
    public String userDashboard(HttpSession session, Model model) {
        DonorRegistration user = (DonorRegistration) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);
        model.addAttribute("user", user);



        if (!model.containsAttribute("donation")) {
            model.addAttribute("donation", new FoodDonar());
        }



        model.addAttribute("donations", donationRepository.findByRestaurantId(user.getId()));

        return "DonarDashboard";
    }

    @PostMapping("/update-profile")
    public String showUpdateProfileForm(@ModelAttribute DonorRegistration user, HttpSession session,@RequestParam("file") MultipartFile file, @RequestParam String section) throws IOException {
     //handle file upload
        if(!file.isEmpty()) {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            user.setFilepath(uploadDir + filename);
            user.setFileName(filename);
            userRepository.save(user);
        }
        userRepository.save(user);
        session.setAttribute("user", user);
        return "redirect:/dashboard/user?section=" + section;

    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {
        String adminUsername = (String) session.getAttribute("admin");
        if (adminUsername == null) {
            return "redirect:/auth/loginadmin";
        }
        model.addAttribute("users", userRepository.findAll());
        return "AdminDashboard.html";
    }

    @GetMapping("/ngo")
    public String ngoDashboard(HttpSession session, Model model) {
        NGORegistration ngo = (NGORegistration) session.getAttribute("user");
        // 1. Security check: Ensure the user is logged in as an NGO
        Long ngoId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");

        if (ngoId == null || !"NGO".equals(role)) {
            return "redirect:/login";
        }

        // 2. Find all AllocationRequests for this NGO
        List<AllocationRequest> allocations = allocationRepo.findByNgoId(ngoId);

        // 3. From those allocations, fetch the actual Food details from DonationRepository
        List<FoodDonar> allocatedFoodItems = new ArrayList<>();

        for (AllocationRequest request : allocations) {
            // Fetch food detail by the foodId stored in the allocation
            Optional<FoodDonar> foodDetail = donationRepository.findById(request.getFoodId());

            // If the food exists, add it to our list (optionally you can attach the status from the request)
            foodDetail.ifPresent(allocatedFoodItems::add);
        }

        // 4. Add the list to the model to display on the dashboard
        model.addAttribute("allocatedFood", allocatedFoodItems);
        model.addAttribute("totalAllocations", allocatedFoodItems.size());

        model.addAttribute("ngo", ngo);
        model.addAttribute("availableFoodList", donationRepository.findAll());
        return "test2";
    }


    /**
     * Marks the food as rescued.
     * Updates the status in both the Allocation table and the Donor's Food table.
     */
    @GetMapping("/rescue/{foodId}")
    public String rescueFood(@PathVariable Long foodId, HttpSession session) {
        Long ngoId = (Long) session.getAttribute("userId");
        if (ngoId == null) return "redirect:/login";

        // 1. Update the donor's food status so the Donor knows it is rescued
        donationRepository.findById(foodId).ifPresent(food -> {
            food.setStatusrequest("RESCUED"); // Assuming statusrequest is the field name for status
            donationRepository.save(food);
        });

        // 2. Update the Allocation Request status to keep records consistent
        // We find the specific allocation for this NGO and this food
        List<AllocationRequest> requests = allocationRepo.findByNgoId(ngoId);
        for (AllocationRequest req : requests) {
            if (req.getFoodId().equals(foodId)) {
                req.setStatus("RESCUED");
                allocationRepo.save(req);
            }
        }

        return "redirect:/dashboard/ngo";
    }
    @GetMapping("/NGO")
    public String ngoUpdateForm(HttpSession session, Model model) {
        NGORegistration ngo = (NGORegistration) session.getAttribute("ngo");
        if (ngo == null) {
            return "redirect:/auth/NGO";
        }
        model.addAttribute("ngo", ngo);
        return "NGO_Registration_Update";
    }

    @PostMapping("/update_ngo")
    public String updateNGODashboard(@ModelAttribute NGORegistration ngo, HttpSession session,@RequestParam("file") MultipartFile file) throws IOException {
        //handle file upload
        if(!file.isEmpty()) {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            ngo.setFilepath(uploadDir + filename);
            ngo.setFileName(filename);
            ngoRepository.save(ngo);
        }
        ngoRepository.save(ngo);
        session.setAttribute("ngo", ngo);
        return "redirect:/dashboard/NGO";
    }

    @GetMapping("/showDonation/{id}")
    public String editDonation(@PathVariable Long id, Model model,HttpSession session) {
        DonorRegistration user = (DonorRegistration)session.getAttribute("user");
        Optional<FoodDonar> donation = donationRepository.findById(id);

        model.addAttribute("food", donation);
        model.addAttribute("user", user);
        return "redirect:/dashboard/user";

    }







}
