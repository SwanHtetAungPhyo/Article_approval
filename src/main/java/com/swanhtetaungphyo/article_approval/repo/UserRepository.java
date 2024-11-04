package com.swanhtetaungphyo.article_approval.repo;

import com.swanhtetaungphyo.article_approval.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}