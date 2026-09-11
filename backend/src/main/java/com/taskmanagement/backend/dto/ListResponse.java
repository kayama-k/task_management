package com.taskmanagement.backend.dto;

import com.taskmanagement.backend.entity.TaskList;

import java.util.List;

/**
 * Read-only representation of a {@link TaskList} returned by the API,
 * with its cards nested in position order.
 *
 * <p>{@code from(TaskList)} must be called while the owning JPA session is
 * still open (see {@code TaskListQueryService}), since {@code list.getCards()}
 * is lazily loaded.
 */
public record ListResponse(Long id, String title, Integer position, List<CardResponse> cards) {

    public static ListResponse from(TaskList list) {
        return new ListResponse(
                list.getId(),
                list.getTitle(),
                list.getPosition(),
                list.getCards().stream().map(CardResponse::from).toList());
    }
}
