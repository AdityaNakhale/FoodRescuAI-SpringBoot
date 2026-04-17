package org.example.project3.Controller;

import org.example.project3.Model.FoodDonar;
import org.example.project3.Model.DonorRegistration;
import org.example.project3.Model.NGORegistration;
import org.example.project3.Repository.DonationRepository;
import org.example.project3.Repository.FoodRepository;
import org.example.project3.Repository.NGORepository;
import jakarta.servlet.http.HttpSession;
import org.example.project3.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class FoodController {

        private static String UPLOAD_DIR = "uploads2/";

        @Autowired
        FoodRepository foodRepository;

    @GetMapping("/")
    public String showFoodPage() {
        return "Home";
    }

    @GetMapping("/foodregistration")
    public String showFoodRegistrationForm(Model model) {
        model.addAttribute("donor", new DonorRegistration());
        return "foodregister";
    }
    @PostMapping("/register/donor")
    public String submitRegistrationForm(@ModelAttribute DonorRegistration user, @RequestParam("licenseFile") MultipartFile file, Model model) {
        try {
            // 1. Call storeFile to save the physical file and get the path string
            String savedPath = fileService.storeFile(file);

            // 2. Call getFileName to get the original name
            String originalName = fileService.getFileName(file);

            // 3. Set these values in your Model object before saving to Database
            user.setFilepath(savedPath);
            user.setFileName(originalName);
            foodRepository.save(user);
            model.addAttribute("submitted", true);
            model.addAttribute("user", user);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("submitted", "file upload failed");
            return "register";
        }

        model.addAttribute("user", user);
        return "success";
    }
    @Autowired
    DonationRepository donationRepository;

    @PostMapping("/donateFood")
    public String donateFood(
            @ModelAttribute FoodDonar donation,
            @RequestParam("file") MultipartFile file,

            // ✅ ADD THESE (important)
            @RequestParam(required = false) String foodTypeOther,
            @RequestParam(required = false) String mealTypeOther,
            @RequestParam(required = false) String categoryOther,

            Model model,
            HttpSession session) {

        try {

            // ✅ HANDLE "Other" VALUES
            if ("Other".equals(donation.getFoodType()) && foodTypeOther != null && !foodTypeOther.isEmpty()) {
                donation.setFoodType(foodTypeOther);
            }

            if ("Other".equals(donation.getMealType()) && mealTypeOther != null && !mealTypeOther.isEmpty()) {
                donation.setMealType(mealTypeOther);
            }

            if ("Other".equals(donation.getCategory()) && categoryOther != null && !categoryOther.isEmpty()) {
                donation.setCategory(categoryOther);
            }

            // ✅ FILE UPLOAD
            // 1. Call storeFile to save the physical file and get the path string
            String savedPath = fileService.storeFile(file);

            // 2. Call getFileName to get the original name
            String originalName = fileService.getFileName(file);

            // 3. Set these values in your Model object before saving to Database
            donation.setFilepath(savedPath);
            donation.setFileName(originalName);

            // ✅ SESSION USER ID
            Long id = (Long) session.getAttribute("userId");

            donation.setRestaurantId(id);
            donation.setStatusrequest("Pending");

            // ✅ SAVE
            donationRepository.save(donation);

            model.addAttribute("submitted", true);
            model.addAttribute("donation", donation);
            DonorRegistration user= (DonorRegistration) session.getAttribute("user");
            session.setAttribute(("user"), user);


        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("submitted", "file upload failed");
            return "fooddonation";
        }

        return "redirect:/dashboard/user?section=donatedFoodSection";
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get("uploads2/").resolve(fileName);
        Resource resource = (Resource) new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header("Content-Disposition", "inline; filename=\"" + fileName + "\"").body(resource);
    }

    @GetMapping("/cancelDonation/{id}")
    public String deleteDonation(@PathVariable Long id,@RequestParam String section) {
        donationRepository.deleteById(id);
        return "redirect:/dashboard/user?section=" + section;
    }
    @GetMapping("/NGORegistration") // NO brace here
    public String showNGORegistration(Model model,HttpSession session) {
        model.addAttribute("ngo", new NGORegistration());
        return "NGO_Registration";
    }

    @Autowired
    NGORepository ngoRepository;

    @Autowired
    FileStorageService fileService;

    @PostMapping("/register/ngo")
    public String registerNGO(@ModelAttribute NGORegistration ngo, @RequestParam("verificationFile") MultipartFile certFile, Model model) {
        try {
            // 1. Call storeFile to save the physical file and get the path string
            String savedPath = fileService.storeFile(certFile);

            // 2. Call getFileName to get the original name
            String originalName = fileService.getFileName(certFile);

            // 3. Set these values in your Model object before saving to Database
            ngo.setFilepath(savedPath);
            ngo.setFileName(originalName);
            // Print to the IntelliJ Console
            System.out.println("--- REGISTRATION ATTEMPT ---");
            System.out.println("NGO Name: " + ngo.getNgoName());
            System.out.println("Lat from Form: " + ngo.getLatitude());
            System.out.println("Lng from Form: " + ngo.getLongitude());

            ngoRepository.save(ngo);
            model.addAttribute("submitted", true);
            model.addAttribute("user", ngo);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("submitted", "file upload failed");
            return "register";
        }



        model.addAttribute("ngo", ngo);
        return "success";
    }

    @GetMapping("/editDonation/{id}")
    public String editDonation(@PathVariable Long id, Model model, HttpSession session, @RequestParam String section, RedirectAttributes redirectAttributes) {
        Optional<FoodDonar> donationOpt = donationRepository.findById(id);

        if (donationOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("donation", donationOpt.get());
        }

        return "redirect:/dashboard/user?section=" + section;
    }


    }



