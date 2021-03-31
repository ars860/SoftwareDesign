package com.example.mvc.service;

import com.example.mvc.domain.Thing;
import com.example.mvc.domain.ThingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ThingService {
    private final ThingRepository thingRepository;

    public void editThing(Long id, String desc, Boolean done) {
        Thing thing = thingRepository.findById(id).get();
        thing.setDescription(desc);
        thing.setDone(done);
        thing.setId(id);

        thingRepository.save(thing);
    }
}
