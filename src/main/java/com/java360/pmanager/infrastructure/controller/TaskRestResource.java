package com.java360.pmanager.infrastructure.controller;

import com.java360.pmanager.domain.applicationservice.TaskService;
import com.java360.pmanager.domain.entity.Task;
import com.java360.pmanager.infrastructure.dto.SaveTaskDataDTO;
import com.java360.pmanager.infrastructure.dto.TaskDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.java360.pmanager.infrastructure.controller.RestConstants.PATH_TASKS;

@RestController
@RequestMapping(PATH_TASKS)
@RequiredArgsConstructor
public class TaskRestResource {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid SaveTaskDataDTO saveTaskData) {
        Task task = taskService.createTask(saveTaskData);

        return ResponseEntity
            .created(URI.create(PATH_TASKS + "/" + task.getId()))
            .body(TaskDTO.create(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> loadTask(@PathVariable("id") String taskId) {
        Task task = taskService.loadTask(taskId);
        return ResponseEntity.ok(TaskDTO.create(task));
    }

}
