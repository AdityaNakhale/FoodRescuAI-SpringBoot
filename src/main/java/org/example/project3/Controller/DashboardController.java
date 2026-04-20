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
    public String userDashboard(HttpSession session, Model model,
                                @RequestParam(required = false) String section) {
        DonorRegistration user = (DonorRegistration) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Set session and model attributes
        session.setAttribute("userId", user.getId());
        model.addAttribute("user", user);

        // 1. If 'donation' wasn't passed via FlashAttributes (from Edit), provide a new one
        if (!model.containsAttribute("donation")) {
            FoodDonar newDonation = new FoodDonar();
            newDonation.setRestaurantId(user.getId()); // Pre-set the owner ID
            model.addAttribute("donation", newDonation);
        }

        // 2. Load the specific user's donation history
        model.addAttribute("donations", donationRepository.findByRestaurantId(user.getId()));

        // 3. Determine which tab to show. If null, default to dashboardSection
        model.addAttribute("activeSection", (section != null) ? section : "dashboardSection");

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
        // 1. Fetch user identification from session
        Long ngoId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");

        if (ngoId == null || !"NGO".equals(role)) {
            return "redirect:/login";
        }

        // 2. Fetch all allocation records for this specific NGO
        List<AllocationRequest> allocations = allocationRepo.findByNgoId(ngoId);

        // 3. Initialize separate lists for pending and rescued items
        List<FoodDonar> pendingFood = new ArrayList<>();
        List<FoodDonar> rescuedFood = new ArrayList<>();
      //  List<FoodDonar> allocatedFoodItems = new ArrayList<>();


        for (AllocationRequest request : allocations) {
            Optional<FoodDonar> foodDetail = donationRepository.findById(request.getFoodId());
            // If the food exists, add it to our list
            //  foodDetail.ifPresent(allocatedFoodItems::add);

            if (foodDetail.isPresent()) {
                FoodDonar food = foodDetail.get();

                // Determine which list the food belongs to based on the Allocation status
                if ("RESCUED".equalsIgnoreCase(request.getStatus())) {
                    rescuedFood.add(food);
                } else {
                    pendingFood.add(food);
                }
            }
        }

        // 4. Pass data to the view (test2.html)
        model.addAttribute("allocatedFood", pendingFood);
        model.addAttribute("pendingFood", pendingFood);
        model.addAttribute("rescuedFood", rescuedFood);
        model.addAttribute("totalAllocations", pendingFood.size());
        model.addAttribute("totalRescued", rescuedFood.size());

        // Also fetch all available food for the Marketplace section
        model.addAttribute("availableFoodList", donationRepository.findAll());
        model.addAttribute("ngo", session.getAttribute("user"));

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
            food.setStatusrequest("RESCUED");
            donationRepository.save(food);
        });

        // 2. Update the Allocation Request status to keep records consistent
        List<AllocationRequest> requests = allocationRepo.findByNgoId(ngoId);
        for (AllocationRequest req : requests) {
            if (req.getFoodId().equals(foodId)) {
                req.setStatus("RESCUED");
                allocationRepo.save(req);
            }
        }

        return "redirect:/dashboard/ngo";
    }


    /**
     * Handles the "Claim for Rescue" hyperlinking from the Marketplace.
     * Maps to: th:href="@{/ngo/claim/{id}(id=${food.id})}"
     */
    @GetMapping("/claim/{id}")
    public String claimFood(@PathVariable Long id, HttpSession session) {
        Long ngoId = (Long) session.getAttribute("userId");
        if (ngoId == null) return "redirect:/login";

        // 1. Create a link record in the AllocationRequest table
        AllocationRequest newRequest = new AllocationRequest();
        newRequest.setFoodId(id);
        newRequest.setNgoId(ngoId);
        newRequest.setStatus("CLAIMED"); // Initial status

        allocationRepo.save(newRequest);

        // 2. Notify the Donor by updating the status in the main Food record
        donationRepository.findById(id).ifPresent(food -> {
            food.setStatusrequest("CLAIMED");
            donationRepository.save(food);
        });

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
