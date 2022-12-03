package ru.practicum.ewmmain.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}