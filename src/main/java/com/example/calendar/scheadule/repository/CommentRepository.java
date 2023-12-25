package com.example.calendar.scheadule.repository;

import com.example.calendar.scheadule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndSchedule_IdAndUser_Id(Long commentId, Long scheduleId, Long id);

    List<Comment> findAllBySchedule_Id(Long scheduleId);
}
