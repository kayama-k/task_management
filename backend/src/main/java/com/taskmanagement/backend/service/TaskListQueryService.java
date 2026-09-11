package com.taskmanagement.backend.service;

import com.taskmanagement.backend.dto.ListResponse;
import com.taskmanagement.backend.repository.TaskListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskListQueryService {

    private final TaskListRepository taskListRepository;

    public TaskListQueryService(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    /**
     * Returns all lists (position order) with their cards (position order)
     * nested. Mapping to {@link ListResponse} happens inside this
     * transaction, while the Hibernate session is still open, so the lazily
     * loaded {@code cards} collection can be initialized safely.
     */
    @Transactional(readOnly = true)
    public List<ListResponse> findAllLists() {
        return taskListRepository.findAllByOrderByPositionAsc()
                .stream()
                .map(ListResponse::from)
                .toList();
    }
}
