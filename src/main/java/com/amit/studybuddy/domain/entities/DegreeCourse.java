package com.amit.studybuddy.domain.entities;

import com.amit.studybuddy.domain.enums.DegreeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "degree_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DegreeCourse {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DegreeType degree;

    @Column(nullable = false)
    private int studyYear;

    @ManyToOne(optional = false)
    private Course course;
}
