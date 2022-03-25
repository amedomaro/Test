package com.bestsite.deploy.repository;

import com.bestsite.deploy.model.Comment;
import com.bestsite.deploy.model.Overview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByOverview(Overview overview);
}
