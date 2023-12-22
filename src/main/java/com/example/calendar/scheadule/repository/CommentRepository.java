package com.example.calendar.scheadule.repository;

import com.example.calendar.scheadule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndSchedule_IdAndUser_Id(Long commentId, Long scheduleId, Long id);
}
