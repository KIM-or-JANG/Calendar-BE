package com.example.calendar.scheadule.entity;

import com.example.calendar.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String userName;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_Id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Schedule schedule;
    //createComment
    public Comment(String userName, String comment, User user, Schedule schedule) {
        this.userName = userName;
        this.comment = comment;
        this.user = user;
        this.schedule = schedule;
    }
    //updateComment
    public void update(String comment) {
        this.comment = comment;
    }
}
