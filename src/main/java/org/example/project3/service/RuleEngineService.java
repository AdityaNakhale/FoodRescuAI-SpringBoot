package org.example.project3.service;

import org.example.project3.Model.DonorRegistration;
import org.example.project3.Model.FoodDonar;
import org.example.project3.Model.NGORegistration;
import org.example.project3.service.DistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class RuleEngineService {
    private final DistanceService distanceService;

    public NGORegistration allocateFood(FoodDonar food, List<NGORegistration> ngos, DonorRegistration user) {
        // Step 1: Pre-filter by physical capacity (Save API costs)
        List<NGORegistration> eligible = ngos.stream()
                .filter(n -> n.getCapacity() >= food.getQuantity())
                .collect(Collectors.toList());

        if (eligible.isEmpty()) return null;

        // Step 2: Set origin from browser coordinates
        String origin = user.getLatitude() + "," + user.getLongitude();

        // Step 3: Trigger batch API calls for road distance/time
        distanceService.calculateRoadMetrics(origin, eligible);

        // Step 4: Rule-Based Scoring Engine
        return eligible.stream()
                .max(Comparator.comparingInt(ngo -> calculateScore(food, ngo)))
                .orElse(null);
    }

    /**
     * Scoring Rules:
     * - Distance: < 5km (+50), < 15km (+30)
     * - Time: < 30 mins (+40)
     * - Expiry Urgency: < 4hrs expiry gives bonus for very close NGOs (+60)
     * - Food Type: Cooked food adds priority (+20)
     * - Dynamic Bonus: (100 - km) ensures closer is always better even within thresholds
     */
    private int calculateScore(FoodDonar food, NGORegistration ngo) {
        int score = 0;

        // Road Distance Logic
        if (ngo.getTravelDistanceKm() < 5) score += 50;
        else if (ngo.getTravelDistanceKm() < 15) score += 30;

        // Efficiency Logic
        if (ngo.getTravelTimeSeconds() < 1800) score += 40;

        // Urgency Logic
        if (food.getExpiryTime() < 4) {
            score += (ngo.getTravelDistanceKm() < 3) ? 60 : 10;
        }

        // Stability Logic
        if ("cooked".equalsIgnoreCase(food.getFoodType())) {
            score += 20;
        }

        // Linear proximity reward
        score += (int) Math.max(0, 100 - ngo.getTravelDistanceKm());

        return score;
    }
}