package org.example.project3.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Date;
import lombok.Data;

@Entity
@Data
public class FoodDonar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;
    private String foodType;
    private String mealType;
    private String category;
    private int quantity;
    private String preparedTime;
    private int expiryTime;
    private String address;
    private String city;
    private Date pickupDate;
    private String pickupTime;
    private String contactName;
    private String mobile;
    private String instructions;
    private String statusrequest;

    private String fileName;
    private String filepath;
    private Long restaurantId;
    private Long assignedNgoId;


}
