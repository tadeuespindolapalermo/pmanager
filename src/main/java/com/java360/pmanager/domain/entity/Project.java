package com.java360.pmanager.domain.entity;

import com.java360.pmanager.domain.model.ProjectStatus;

import java.time.LocalDate;

public class Project {

    private String id;

    private String name;

    private String description;

    private LocalDate initialDate;

    private LocalDate finalDate;

    private ProjectStatus status;
}
