package com.bestsite.deploy.repository;

import com.bestsite.deploy.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {

    Optional<Page> findByName(String name);
}
