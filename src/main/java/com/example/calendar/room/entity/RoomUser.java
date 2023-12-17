package com.example.calendar.room.entity;

import com.example.calendar.room.entity.Room;
import com.example.calendar.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_Id")
    private Room room;

    @Column
    private boolean manager;

    //createRoom
    public RoomUser(Room room, boolean b) {
        this.user = room.getManager();
        this.room = room;
        this.manager = b;
    }
    //invateRoom

    public RoomUser(User user, Room room, boolean manager) {
        this.user = user;
        this.room = room;
        this.manager = manager;
    }
}
