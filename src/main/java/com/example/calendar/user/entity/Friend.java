package com.example.calendar.user.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column
    private String friendName;
    @Column
    private String friendEmail;
    @Column
    private String friendProfileImage;

}
