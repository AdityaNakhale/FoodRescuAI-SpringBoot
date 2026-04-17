package org.example.project3.Repository;

import org.example.project3.Model.NGORegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NGORepository extends JpaRepository<NGORegistration, Long> {
    NGORegistration findByEmailAndPassword(String email, String password);

    @Query("SELECT n FROM NGORegistration n WHERE " +
            "LOWER(n.ngoName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(n.registrationNumber) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(n.city) LIKE LOWER(CONCAT('%', :kw, '%'))")
    List<NGORegistration> searchNGOs(@Param("kw") String keyword);
}

