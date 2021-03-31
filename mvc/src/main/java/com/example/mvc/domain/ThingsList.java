package com.example.mvc.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThingsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description = "";

    @OneToMany (mappedBy = "thingsList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Thing> things = new ArrayList<>();
}
