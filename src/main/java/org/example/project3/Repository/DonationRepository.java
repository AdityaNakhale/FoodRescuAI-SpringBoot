package org.example.project3.Repository;

import org.example.project3.Model.FoodDonar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<FoodDonar, Long> {
    List<FoodDonar> findByRestaurantId(Long restaurantId);

    @Query("SELECT f FROM FoodDonar f WHERE " +
            "LOWER(f.foodName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(f.foodType) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(f.category) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(f.statusrequest) LIKE LOWER(CONCAT('%', :kw, '%'))")
    List<FoodDonar> searchDonations(@Param("kw") String keyword);
}
