package com.example.mvc.service;

import com.example.mvc.domain.Thing;
import com.example.mvc.domain.ThingsList;
import com.example.mvc.domain.ThingsListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ThingsListService {
    private final ThingsListRepository repository;

    public List<ThingsList> getLists() {
        return repository.findAll();
    }

    public long getListsAmount() {
        return repository.count();
    }

    public ThingsList findById(Long id) {
        return repository.findById(id).get();
    }

    public void addEmptyList() {
        ThingsList thingsList = ThingsList.builder()
                .description("TODO List #" + (getListsAmount() + 1))
                .build();

        repository.save(thingsList);
    }

    public void addThing(ThingsList thingsList) {
        Thing thing = Thing.builder()
                .thingsList(thingsList)
                .description("Something #" + (thingsList.getThings().size() + 1))
                .done(false)
                .build();

        thingsList.getThings().add(thing);
        repository.save(thingsList);
    }

    public void editList(Long id, String desc) {
        ThingsList list = findById(id);

        list.setDescription(desc);
        repository.save(list);
    }

    public void deleteList(Long id) {
        repository.deleteById(id);
    }
}
