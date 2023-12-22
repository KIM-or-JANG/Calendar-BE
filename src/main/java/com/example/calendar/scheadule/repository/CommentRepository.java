package com.example.calendar.scheadule.repository;

import com.example.calendar.scheadule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
