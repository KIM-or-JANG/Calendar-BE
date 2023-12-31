package com.example.calendar.room.entity;

import com.example.calendar.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String roomName;

    @Column
    private String roomProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User manager;

    //createRoom
    public Room(String roomName, String roomprofile, User user) {
        this.roomName = roomName;
        this.roomProfile = roomprofile;
        this.manager = user;
    }
}
