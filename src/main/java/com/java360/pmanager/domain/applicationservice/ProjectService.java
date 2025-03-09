package com.java360.pmanager.domain.applicationservice;

import com.java360.pmanager.domain.entity.Member;
import com.java360.pmanager.domain.entity.Project;
import com.java360.pmanager.domain.exception.DuplicateProjectException;
import com.java360.pmanager.domain.exception.InvalidProjectStatusException;
import com.java360.pmanager.domain.exception.ProjectNotFoundException;
import com.java360.pmanager.domain.model.ProjectStatus;
import com.java360.pmanager.domain.repository.ProjectRepository;
import com.java360.pmanager.infrastructure.dto.SaveProjectDataDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;

    @Transactional
    public Project createProject(SaveProjectDataDTO saveProjectData) {
        if (existsProjectWithName(saveProjectData.getName(), null)) {
            throw new DuplicateProjectException(saveProjectData.getName());
        }

        Project project = Project
            .builder()
            .name(saveProjectData.getName())
            .description(saveProjectData.getDescription())
            .initialDate(saveProjectData.getInitialDate())
            .finalDate(saveProjectData.getFinalDate())
            .status(ProjectStatus.PENDING)
            .build();

        projectRepository.save(project);

        addMemberToProject(saveProjectData.getMemberIds(), project);

        log.info("Project created: {}", project);
        return project;
    }

    public Project loadProject(String projectId) {
        return projectRepository
            .findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Transactional
    public void deleteProject(String projectId) {
        projectRepository.delete(loadProject(projectId));
    }

    @Transactional
    public Project updateProject(String projectId, SaveProjectDataDTO saveProjectData) {
        if (existsProjectWithName(saveProjectData.getName(), projectId)) {
            throw new DuplicateProjectException(saveProjectData.getName());
        }

        Project project = loadProject(projectId);

        project.setName(saveProjectData.getName());
        project.setDescription(saveProjectData.getDescription());
        project.setInitialDate(saveProjectData.getInitialDate());
        project.setFinalDate(saveProjectData.getFinalDate());
        project.setStatus(convertToProjectStatus(saveProjectData.getStatus()));

        addMemberToProject(saveProjectData.getMemberIds(), project);

        return project;
    }

    private ProjectStatus convertToProjectStatus(String statusStr) {
        try {
            return ProjectStatus.valueOf(statusStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidProjectStatusException(statusStr);
        }
    }

    private boolean existsProjectWithName(String name, String idToExclude) {
        return projectRepository
            .findByName(name)
            .filter(p -> !Objects.equals(p.getId(), idToExclude))
            .isPresent();
    }

    private void addMemberToProject(Set<String> memberIds, Project project) {
        List<Member> members = Optional
            .ofNullable(memberIds)
            .orElse(Set.of())
            .stream()
            .map(memberService::loadMemberById)
            .toList();

        project.setMembers(members);
    }
}
