package org.example.project3.service;

import org.example.project3.Model.NGORegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Service
class DistanceService {
    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final int MAX_BATCH_SIZE = 25;

    /**
     * Calculates road metrics for all eligible NGOs by splitting them into batches.
     */
    public void calculateRoadMetrics(String origin, List<NGORegistration> ngos) {
        if (ngos.isEmpty()) return;

        for (int i = 0; i < ngos.size(); i += MAX_BATCH_SIZE) {
            int end = Math.min(i + MAX_BATCH_SIZE, ngos.size());
            List<NGORegistration> batch = ngos.subList(i, end);
            fetchBatchMetrics(origin, batch);
        }
    }

    private void fetchBatchMetrics(String origin, List<NGORegistration> batch) {
        String destinations = batch.stream()
                .map(NGORegistration::getLocationString)
                .filter(loc -> loc != null)
                .collect(Collectors.joining("|"));

        if (destinations.isEmpty()) return;

        String url = String.format(
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
                origin, destinations, apiKey
        );

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);

            if (!"OK".equals(json.getString("status"))) {
                setFallbackMetrics(batch);
                return;
            }

            JSONArray elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

            for (int i = 0; i < batch.size(); i++) {
                JSONObject element = elements.getJSONObject(i);
                NGORegistration ngo = batch.get(i);

                if ("OK".equals(element.getString("status"))) {
                    ngo.setTravelDistanceKm(element.getJSONObject("distance").getDouble("value") / 1000.0);
                    ngo.setTravelTimeSeconds(element.getJSONObject("duration").getLong("value"));
                } else {
                    ngo.setTravelDistanceKm(999.0);
                }
            }
        } catch (Exception e) {
            setFallbackMetrics(batch);
        }
    }

    private void setFallbackMetrics(List<NGORegistration> batch) {
        batch.forEach(n -> n.setTravelDistanceKm(999.0));
    }
}
