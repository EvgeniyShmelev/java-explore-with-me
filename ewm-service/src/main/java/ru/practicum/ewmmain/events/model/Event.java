package ru.practicum.ewmmain.events.model;

import lombok.Data;
import lombok.ToString;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.compilation.model.Compilation;
import ru.practicum.ewmmain.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "createdOn")
    private LocalDateTime createdOn;
    @Column(name = "description", nullable = false, length = 9999)
    private String description;
    @Column(name = "eventDate", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Embedded
    private Location location;
    @Column(name = "paid")
    private Boolean paid; //платное мероприятие?
    @Column(name = "title", nullable = false, length = 120)
    private String title;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "publishedOn")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventStatus state;
    @Column(name = "views")
    private Long views;
    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    Set<Compilation> compilations;
}
