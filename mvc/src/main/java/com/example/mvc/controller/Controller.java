package com.example.mvc.controller;

import com.example.mvc.domain.ThingsList;
import com.example.mvc.service.ThingService;
import com.example.mvc.service.ThingsListService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {
    private final ThingsListService listsService;
    private final ThingService thingService;

    @GetMapping("/")
    public String index(Model model) {
        List<ThingsList> lists = listsService.getLists();
        model.addAttribute("thingLists", lists);
        return "index";
    }

    @PostMapping("/add-empty-list")
    public String addEmptyList() {
        listsService.addEmptyList();
        return "redirect:/";
    }

    @PostMapping("/add-empty-thing")
    public String addEmptyThing(@RequestParam("thingListId") Long thingListId) {
        ThingsList thingsList = listsService.findById(thingListId);
        listsService.addThing(thingsList);
        return "redirect:/";
    }

    @PostMapping("/edit-thing")
    public String editThing(@RequestParam("thingId") Long thingId,
                            @RequestParam("description") String description,
                            @RequestParam(name = "done", required = false) Boolean done) {
        thingService.editThing(thingId, description, done);
        return "redirect:/";
    }

    @PostMapping("/edit-list")
    public String editList(@RequestParam("listId") Long listId,
                           @RequestParam("description") String description) {
        listsService.editList(listId, description);
        return "redirect:/";
    }

    @PostMapping("/delete-list")
    public String deleteList(@RequestParam("listId") Long listId) {
        listsService.deleteList(listId);
        return "redirect:/";
    }
}
