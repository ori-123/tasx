package com.codecool.tasx.exception.project;

public class ProjectJoinRequestNotFoundException extends RuntimeException {
    private final Long id;
    public ProjectJoinRequestNotFoundException(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
