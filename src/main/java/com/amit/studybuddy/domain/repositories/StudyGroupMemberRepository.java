package com.amit.studybuddy.domain.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyGroupMemberRepository extend JpaRepository<StudyGroupMember, UUID> {
    

}
