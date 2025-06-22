package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.Meeting;
import com.amit.studybuddy.domain.entities.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

        List<Meeting> findByGroupId(UUID groupId);
        boolean existsByGroupIdAndScheduledTime(UUID groupId, LocalDateTime time);
        Optional<Meeting> findFirstByGroupIdAndScheduledTimeAfterOrderByScheduledTimeAsc(UUID groupId, LocalDateTime now);



}

