package com.amit.studybuddy.domain.entities;


import com.amit.studybuddy.domain.enums.MatchStatus;
import com.amit.studybuddy.domain.enums.MeetingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meetings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingType type; // ONLINE or IN_PERSON

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String zoomLink;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;


}

