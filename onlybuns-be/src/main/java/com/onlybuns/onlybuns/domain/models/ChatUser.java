package com.onlybuns.onlybuns.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id", nullable = false)
    private ChatRoom chatRoom; // ChatRoom na koji je korisnik pridružen

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user; // Korisnik koji je pristupio chat sobi

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt; // Vreme kada je korisnik pristupio chat sobi

    @Column(name = "last_active", nullable = true)
    private LocalDateTime lastActive; // Vreme kada je korisnik poslednji put bio aktivan
}
