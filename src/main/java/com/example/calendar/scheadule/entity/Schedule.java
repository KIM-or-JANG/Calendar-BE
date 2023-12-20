package com.example.calendar.scheadule.entity;

import com.example.calendar.room.entity.Room;
import com.example.calendar.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
public class Schedule {  //사용자 일정 entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String schedule;   //일정

    @Column
    private String locdate;    //날짜

    @ManyToOne
    @JoinColumn(name = "room_Id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

   @ManyToOne
   @JoinColumn(name = "user_Id")
   private User user;

   //createSchedule
    public Schedule(String schedule, String locdate, Room room, User user) {
        this.schedule = schedule;
        this.locdate = locdate;
        this.room = room;
        this.user = user;
    }
}
