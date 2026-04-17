package org.example.project3.Repository;

import org.example.project3.Model.AllocationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllocationRequestRepository extends JpaRepository<AllocationRequest, Long> {
    List<AllocationRequest> findByNgoId(Long ngoId);
}
