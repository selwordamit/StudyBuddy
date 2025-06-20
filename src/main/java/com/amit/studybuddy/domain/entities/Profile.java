package com.amit.studybuddy.domain.entities;


import jakarta.persistence.*;
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

    private String bio;
    private String profilePictureUrl;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String institution; // The institution the user is associated with

    @Column(nullable = false)
    private String degree; // The degree the user is pursuing

    @Column(nullable = false)
    private int studyYear; // The year of study the user is currently in

    @Column(nullable = false)
    private Boolean verified; // Indicates if the profile is verified

    @Column(nullable = false)
    private Boolean emailNotificationsEnabled; // Indicates if email notifications are enabled for the profile

    @Column(nullable = false)
    private Boolean pushNotificationsEnabled; // Indicates if push notifications are enabled for the profile



}
