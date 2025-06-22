package com.amit.studybuddy.repositories;


import com.amit.studybuddy.domain.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findByRatedId(UUID userId);
    List<Rating> findByRaterId(UUID userId);

}
