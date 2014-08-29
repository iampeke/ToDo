package com.example.rrich.toapp2;

/**
 * Created by rrich on 8/28/14.
 */
public class ToDoItem {
    private String toDoTask;
    private boolean highPriority;

    public ToDoItem() {

    }

    public ToDoItem(String toDo, boolean pr) {
        this.toDoTask = toDo;
        this.highPriority = pr;
    }

    public String getToDoTask() {
        return this.toDoTask;
    }

    public void setToDoTask(String toDo) {
        this.toDoTask = toDo;
    }

    public boolean getHighPriority() {
        return this.highPriority;
    }

    public void setHighPriority(boolean hp) {
        this.highPriority = hp;
    }

}
