package com.codecool.tasx.exception.task;

public class TaskNotEditableException extends RuntimeException{
    private final Long id;

    public TaskNotEditableException(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
