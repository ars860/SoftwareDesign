package com.example.mvc.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingRepository extends CrudRepository<Thing, Long> {
}
