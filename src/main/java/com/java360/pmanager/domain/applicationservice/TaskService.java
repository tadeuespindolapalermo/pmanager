package com.java360.pmanager.domain.applicationservice;

import com.java360.pmanager.domain.entity.Member;
import com.java360.pmanager.domain.entity.Project;
import com.java360.pmanager.domain.entity.Task;
import com.java360.pmanager.domain.exception.InvalidTaskStatusException;
import com.java360.pmanager.domain.exception.TaskNotFoundException;
import com.java360.pmanager.domain.model.TaskStatus;
import com.java360.pmanager.domain.repository.TaskRepository;
import com.java360.pmanager.infrastructure.dto.SaveTaskDataDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.java360.pmanager.domain.model.TaskStatus.PENDING;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final MemberService memberService;
    private final ProjectService projectService;
    private final TaskRepository taskRepository;

    @Transactional
    public Task createTask(SaveTaskDataDTO saveTaskData) {
        Project project = null;
        Member member = null;

        if (nonNull(saveTaskData.getProjectId())) {
            project = projectService.loadProject(saveTaskData.getProjectId());
        }

        if (nonNull(saveTaskData.getMemberId())) {
            member = memberService.loadMemberById(saveTaskData.getMemberId());
        }

        Task task = Task
            .builder()
            .title(saveTaskData.getTitle())
            .description(saveTaskData.getDescription())
            .numberOfDays(saveTaskData.getNumberOfDays())
            .status(PENDING)
            .project(project)
            .assignedMember(member)
            .build();

        taskRepository.save(task);

        return task;
    }

    public Task loadTask(String taskId) {
        return taskRepository
            .findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Transactional
    public void deleteTask(String taskId) {
        taskRepository.delete(loadTask(taskId));
    }

    @Transactional
    public Task updateTask(String taskId, SaveTaskDataDTO saveTaskData) {
        Task task = loadTask(taskId);

        task.setTitle(saveTaskData.getTitle());
        task.setDescription(saveTaskData.getDescription());
        task.setNumberOfDays(saveTaskData.getNumberOfDays());
        task.setStatus(convertToTaskStatus(saveTaskData.getStatus()));

        return task;
    }

    private TaskStatus convertToTaskStatus(String statusStr) {
        try {
            return TaskStatus.valueOf(statusStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskStatusException(statusStr);
        }
    }

}
