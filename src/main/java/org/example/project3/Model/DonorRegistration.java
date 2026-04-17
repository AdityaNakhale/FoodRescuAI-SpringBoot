package org.example.project3.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class DonorRegistration {
        private String fullName;
        private String email;
        private String mobile;
        private String password;
        private String address;
        private String city;
        private String state;
        private String pincode;

        private String donorType;
        private String organizationName;
        private String foodType;
        private String quantity;
        private String pickupTime;
        private String fssai;

        private String fileName;
        private String filepath;

        private Double latitude;
        private Double longitude;
        private Double altitude;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
