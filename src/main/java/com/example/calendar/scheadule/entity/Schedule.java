package com.example.calendar.Scheadule.entity;

import com.example.calendar.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Schedule {  //사용자 일정 entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String dataTitle;   //일정 제목

    @Column
    private String date;    //날짜

    @ManyToOne
    @JoinColumn(name = "room_Id")
    private Room room;

   @ManyToOne
   @JoinColumn(name = "user_Id")
   private User user;

}
