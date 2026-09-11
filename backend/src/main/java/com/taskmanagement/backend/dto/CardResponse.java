package com.taskmanagement.backend.dto;

import com.taskmanagement.backend.entity.Card;

import java.time.LocalDateTime;

/**
 * Read-only representation of a {@link Card} returned by the API.
 * Deliberately excludes the back-reference to its parent list (see
 * {@link ListResponse}) to avoid a serialization cycle.
 */
public record CardResponse(
        Long id,
        String title,
        String description,
        Integer position,
        LocalDateTime dueAt,
        LocalDateTime createdAt) {

    public static CardResponse from(Card card) {
        return new CardResponse(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getPosition(),
                card.getDueAt(),
                card.getCreatedAt());
    }
}
