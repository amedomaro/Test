package com.bestsite.deploy.repository;

import com.bestsite.deploy.model.Overview;
import com.bestsite.deploy.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OverviewRepository extends JpaRepository<Overview, Long> {

    @EntityGraph(attributePaths = {"comments"})
    List<Overview> findAll();

    List<Overview> findAllByAuthor(User user);
}
