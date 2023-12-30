package com.example.calendar.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "from_User_Id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User fromUserID;

    @ManyToOne
    @JoinColumn(name = "to_User_Id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User toUserID;

    @Column
    private boolean weAreFriend = false;

    public Friend(User user, User friend) {
        fromUserID = user;
        toUserID = friend;
    }

    public void update(boolean permisson) {
        this.weAreFriend = permisson;
    }
}
