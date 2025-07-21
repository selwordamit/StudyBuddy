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

    @Override
    public Optional<Match> tryMatch(UUID userId, UUID courseId) {
        log.info("[MATCH_TRY] - [userId={}] [courseId={}] - STARTED", userId, courseId);

        // 1. Get User and Course entities
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        // 2. Look for another waiting user for the same course (not this user)
        Optional<UserCourse> candidateOpt = userCourseRepository.findFirstByCourseIdAndUserIdNot(courseId, userId);

        if (candidateOpt.isPresent()) {
            // Found a candidate to match
            UserCourse candidate = candidateOpt.get();
            User matchedUser = candidate.getUser();

            // 3. Create and save new Match
            Match match = Match.builder()
                    .user(user)
                    .matchedUser(matchedUser)
                    .course(course)
                    .createdAt(LocalDateTime.now())
                    .status(MatchStatus.CONFIRMED)
                    .build();

            matchRepository.save(match);

            // 4. Remove both users from waiting list
            userCourseRepository.delete(candidate);
            userCourseRepository.deleteByUserIdAndCourseId(userId, courseId);

            log.info("[MATCH_SUCCESS] - [userId={}] [matchedUserId={}] [courseId={}] [matchId={}]",
                    userId, matchedUser.getId(), courseId, match.getId());

            return Optional.of(match);

        } else {
            // No candidates found; add user to waiting list if not already there
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

