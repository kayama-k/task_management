package com.taskmanagement.backend.controller;

import com.taskmanagement.backend.dto.ListResponse;
import com.taskmanagement.backend.service.TaskListQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskListController {

    private final TaskListQueryService taskListQueryService;

    public TaskListController(TaskListQueryService taskListQueryService) {
        this.taskListQueryService = taskListQueryService;
    }

    /** GET /api/lists: 全リストを、所属カード込み(position順)で取得(docs/requirements.md 9.1)。 */
    @GetMapping("/api/lists")
    public List<ListResponse> getLists() {
        return taskListQueryService.findAllLists();
    }
}
