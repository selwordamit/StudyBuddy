package com.amit.studybuddy.services.implementations;


import com.amit.studybuddy.domain.dtos.MatchRequest;
import com.amit.studybuddy.domain.dtos.MatchResponse;
import com.amit.studybuddy.domain.entities.Course;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.domain.entities.UserCourse;
import com.amit.studybuddy.domain.enums.MatchStatus;
import com.amit.studybuddy.domain.mappers.MatchMapper;
import com.amit.studybuddy.repositories.CourseRepository;
import com.amit.studybuddy.repositories.MatchRepository;
import com.amit.studybuddy.repositories.UserCourseRepository;
import com.amit.studybuddy.repositories.UserRepository;
import com.amit.studybuddy.services.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final MatchRepository matchRepository;

    /**
     * Attempts to find a match for the user in the given course.
     * If another user is waiting for the same course, a match is created.
     * Otherwise, the current user is added to the waiting list.
     *
     * @param userId    the ID of the user requesting a match
     * @param courseId  the ID of the course to match for
     * @return an Optional Match if one is created, or Optional.empty if user is queued
     */
    @Override
    public Optional<Match> tryMatch(UUID userId, UUID courseId) {
        log.info("[MATCH_TRY] - [userId={}] [courseId={}] - STARTED", userId, courseId);

        // 1. Validate user and course existence
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        // 2. Look for another waiting user for the same course (excluding current user)
        Optional<UserCourse> candidateOpt = userCourseRepository.findFirstByCourseIdAndUserIdNot(courseId, userId);

        if (candidateOpt.isPresent()) {
            // ✅ Found a matchable candidate
            UserCourse candidate = candidateOpt.get();
            User matchedUser = candidate.getUser();

            // 3. Create and save the new Match
            Match match = Match.builder()
                    .user(user)
                    .matchedUser(matchedUser)
                    .course(course)
                    .createdAt(LocalDateTime.now())
                    .status(MatchStatus.CONFIRMED)
                    .build();

            matchRepository.save(match);

            // 4. Remove both users from the waiting list
            userCourseRepository.delete(candidate);
            userCourseRepository.deleteByUserIdAndCourseId(userId, courseId);

            log.info("[MATCH_SUCCESS] - [userId={}] [matchedUserId={}] [courseId={}] [matchId={}]",
                    userId, matchedUser.getId(), courseId, match.getId());

            return Optional.of(match);

        } else {
            // ⚠️ No match found — user will be added to the waiting list
            if (!userCourseRepository.existsByUserIdAndCourseId(userId, courseId)) {
                UserCourse userCourse = UserCourse.builder()
                        .user(user)
                        .course(course)
                        .build();
                userCourseRepository.save(userCourse);
                log.info("[MATCH_WAIT] - [userId={}] [courseId={}] - User added to waiting list", userId, courseId);
            } else {
                log.info("[MATCH_WAIT] - [userId={}] [courseId={}] - User already waiting", userId, courseId);
            }
            return Optional.empty();
        }
    }
}


