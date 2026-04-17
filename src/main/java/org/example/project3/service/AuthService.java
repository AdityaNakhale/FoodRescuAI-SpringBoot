package org.example.project3.service;

import jakarta.servlet.http.HttpSession;
import org.example.project3.Model.DonorRegistration;
import org.example.project3.Model.NGORegistration;
import org.example.project3.Repository.FoodRepository;
import org.example.project3.Repository.NGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private FoodRepository foodRepository;

    /**
     * Attempts to login an NGO or Donor based on credentials.
     */
    public Object login(String email, String password, String type, HttpSession session) {
        if ("NGO".equalsIgnoreCase(type)) {
            NGORegistration ngo = ngoRepository.findByEmailAndPassword(email, password);
            if (ngo != null) {
                setSession(session, ngo.getId(), "NGO", ngo.getNgoName());
                return ngo;
            }
        } else if ("DONOR".equalsIgnoreCase(type)) {
            // Assuming DonorRepository has findByEmailAndPassword
            DonorRegistration donor = foodRepository.findByEmailAndPassword(email, password);
            if (donor != null) {
                setSession(session, donor.getId(), "DONOR", donor.getFullName());
                return donor;
            }
        }
        return null;
    }

    /**
     * Handle Admin login (usually checked against hardcoded config or special table).
     */
    public boolean loginAdmin(String email, String password, HttpSession session) {
        if ("admin@foodbridge.ai".equals(email) && "admin123".equals(password)) {
            setSession(session, 0L, "ADMIN", "System Administrator");
            return true;
        }
        return false;
    }

    private void setSession(HttpSession session, Long id, String role, String name) {
        session.setAttribute("userId", id);
        session.setAttribute("userRole", role);
        session.setAttribute("userName", name);
        session.setAttribute("user", role.equals("NGO") ? ngoRepository.findById(id).orElse(null) : foodRepository.findById(id).orElse(null));
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}