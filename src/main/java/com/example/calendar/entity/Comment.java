package com.example.calendar.entity;

import jakarta.persistence.*;
import lombok.CustomLog;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String userName;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_Id")
    private Schedule schedule;

}
