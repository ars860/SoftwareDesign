package com.example.mvc.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThingsListRepository extends JpaRepository<ThingsList, Long> {
}
