package ru.practicum.ewmmain.compilation.model;


import ru.practicum.ewmmain.events.model.Event;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 1000)
    private String title;
    @Column(name = "pinned")
    private Boolean pinned;
    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Collection<Event> events;
}
