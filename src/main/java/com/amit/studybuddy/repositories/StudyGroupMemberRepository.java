package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.StudyGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyGroupMemberRepository extends JpaRepository<StudyGroupMember, UUID> {

    boolean existsByUserIdAndGroupId(UUID userId, UUID groupId);
    List<StudyGroupMember> findByGroupId(UUID groupId);
    List<StudyGroupMember> findByUserId(UUID userId);
    void deleteByUserIdAndGroupId(UUID userId, UUID groupId);
    void deleteAllByGroupId(UUID groupId);

}
