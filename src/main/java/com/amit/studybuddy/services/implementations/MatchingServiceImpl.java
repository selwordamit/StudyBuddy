package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.entities.Course;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.domain.entities.UserCourse;
import com.amit.studybuddy.domain.enums.MatchStatus;
import com.amit.studybuddy.repositories.CourseRepository;
import com.amit.studybuddy.repositories.MatchRepository;
import com.amit.studybuddy.repositories.UserCourseRepository;
import com.amit.studybuddy.repositories.UserRepository;
import com.amit.studybuddy.services.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
     * If a matchable user exists, a Match is created and returned.
     * Otherwise, the user is added to the waiting list.
     *
     * @param userId   ID of the user requesting the match
     * @param courseId ID of the course
     * @return Optional Match if created, or Optional.empty if user is queued
     */
    @Override
    public Optional<Match> tryMatch(UUID userId, UUID courseId) {
        log.info("[MATCH_TRY] - [userId={}] [courseId={}] - STARTED", userId, courseId);

        // 1. Validate user and course
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        // 2. Check if user already has a match in this course (either side)
        boolean matchExists = matchRepository
                .existsByUserIdAndCourseIdOrMatchedUserIdAndCourseId(userId, courseId, userId, courseId);
        if (matchExists) {
            log.warn("[MATCH_DUPLICATE] - [userId={}] [courseId={}] - Match already exists", userId, courseId);
            return Optional.empty();
        }

        // 3. Try to find a candidate waiting for the same course
        Optional<UserCourse> candidateOpt =
                userCourseRepository.findFirstByCourseIdAndUserIdNot(courseId, userId);

        if (candidateOpt.isPresent()) {
            // A matchable user was found
            UserCourse candidate = candidateOpt.get();
            User matchedUser = candidate.getUser();

            Match match = Match.builder()
                    .user(user)
                    .matchedUser(matchedUser)
                    .course(course)
                    .createdAt(LocalDateTime.now())
                    .status(MatchStatus.PENDING)
                    .build();

            match.setStatus(MatchStatus.COMPLETED);
            matchRepository.save(match);

            // 4. Remove both users from the waiting list
            userCourseRepository.delete(candidate);
            userCourseRepository.deleteByUserIdAndCourseId(userId, courseId);

            log.info("[MATCH_SUCCESS] - [userId={}] [matchedUserId={}] [courseId={}] [matchId={}]",
                    userId, matchedUser.getId(), courseId, match.getId());

            return Optional.of(match);

        } else {
            // No candidate found — add user to waiting list if not already there
            if (!userCourseRepository.existsByUserIdAndCourseId(userId, courseId)) {
                UserCourse userCourse = UserCourse.builder()
                        .user(user)
                        .course(course)
                        .build();
                userCourseRepository.save(userCourse);

                log.info("[MATCH_WAIT] - [userId={}] [courseId={}] - Added to waiting list", userId, courseId);
            } else {
                log.info("[MATCH_WAIT] - [userId={}] [courseId={}] - Already in waiting list", userId, courseId);
            }

            return Optional.empty();
        }
    }

    @Override
    public List<Match> getAllUserMatches(UUID userId) {
        log.info("[MATCH_FETCH_ALL] - [userId={}] - STARTED", userId);
        List<Match> matches = matchRepository.findAllByUserIdOrMatchedUserId(userId, userId);
        log.info("[MATCH_FETCH_ALL] - [userId={}] - FOUND {} MATCHES", userId, matches.size());
        return matches;
    }

}
