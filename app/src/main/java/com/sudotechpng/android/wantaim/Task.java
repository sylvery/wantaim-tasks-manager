package com.sudotechpng.android.wantaim;

/**
 * Created by syagi on 08/15/2017.
 */

public class Task {

    String taskId;
    String taskName;
    Boolean completed;

    public Task(){}

    public Task(String taskId, String taskName, boolean completed) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.completed = completed;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getTaskId() { return taskId; }

    public String getTaskName() {
        return taskName;
    }

}
