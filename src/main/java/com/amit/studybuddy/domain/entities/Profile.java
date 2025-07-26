package com.amit.studybuddy.domain.entities;


import com.amit.studybuddy.domain.enums.DegreeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; //

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String bio;
    private String profilePictureUrl;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String institution; // The institution the user is associated with

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DegreeType degree;

    private int studyYear;

    @Column(nullable = false)
    private Boolean verified; // Indicates if the profile is verified

    @Column(nullable = false)
    private Boolean emailNotificationsEnabled; // Indicates if email notifications are enabled for the profile

    @Column(nullable = false)
    private Boolean pushNotificationsEnabled; // Indicates if push notifications are enabled for the profile

}
