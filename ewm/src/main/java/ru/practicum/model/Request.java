package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Long id;
    @Column(name = "created")
    private LocalDateTime created;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 15)
    private RequestStatus status = RequestStatus.PENDING;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User requester;
}
