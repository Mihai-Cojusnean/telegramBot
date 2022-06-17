package org.example.telegramservice.dbservice.repository;

import org.example.telegramservice.dbservice.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends JpaRepository<Test, Long> {
}
