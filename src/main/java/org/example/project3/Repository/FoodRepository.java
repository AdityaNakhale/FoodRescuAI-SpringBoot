package org.example.project3.Repository;

import org.example.project3.Model.DonorRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<DonorRegistration, Long> {
    DonorRegistration findByEmailAndPassword(String email, String password);

    @Query("SELECT d FROM DonorRegistration d WHERE " +
            "LOWER(d.organizationName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(d.fullName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(d.city) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "d.mobile LIKE CONCAT('%', :kw, '%')")
    List<DonorRegistration> searchDonors(@Param("kw") String keyword);
}
