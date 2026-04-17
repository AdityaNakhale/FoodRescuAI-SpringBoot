package org.example.project3.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class NGORegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Organization Identity ---
    private String ngoName;
    private String registrationNumber;
    private String ngoType;

    // --- Login Credentials ---
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    // --- Contact & Location ---
    private String contactPerson;
    private String mobile;
    private int capacity;

    @Column(length = 500) // Address can be long
    private String address;
    private String city;
    private String pincode;

    // --- Google Maps Coordinates ---
    // Use Double for high precision in GPS coordinates
    private Double latitude;
    private Double longitude;
    private Double altitude;

    // --- File Verification Data ---
    // These store the file details after upload
    private String fileName;
    private String filepath;


    @Transient // Runtime road distance from donor - NOT persisted in DB
    private double travelDistanceKm;
    @Transient // Runtime travel time from donor - NOT persisted in DB
    private long travelTimeSeconds;

    /**
     * Helper for Google Maps API query parameters.
     */
    public String getLocationString() {
        if (latitude == null || longitude == null) return null;
        return latitude + "," + longitude;
    }

}


