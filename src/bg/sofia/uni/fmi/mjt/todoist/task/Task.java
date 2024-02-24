package bg.sofia.uni.fmi.mjt.todoist.task;

import bg.sofia.uni.fmi.mjt.todoist.exception.TimeFrameMismatchException;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private String name;
    private LocalDate date;
    private LocalDate dueDate;
    private String description;

    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "due-date";
    private static final String DESCRIPTION = "description";
    private static final String COMPLETED = "completed";
    private static final String DASH = "-";
    private static final String SPACE = " ";
    private static final int PRIME_NUMBER = 31;

    private boolean completed;

    public Task(TaskBuilder taskBuilder) {
        this.name = taskBuilder.name;
        this.date = taskBuilder.date;
        this.dueDate = taskBuilder.dueDate;
        this.description = taskBuilder.description;
        completed = false;
    }

    public void finish() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        StringBuilder taskString = new StringBuilder();

        taskString.append(NAME).append(DASH).append(name).append(SPACE);

        if (date != null) {
            taskString.append(DATE).append(DASH).append(date).append(SPACE);
        }
        if (dueDate != null) {
            taskString.append(DUE_DATE).append(DASH).append(DUE_DATE).append(SPACE);
        }
        if (description != null) {
            taskString.append(DESCRIPTION).append(DASH).append(description).append(SPACE);
        }

        taskString.append(COMPLETED).append(DASH).append(completed).append(SPACE);

        return taskString.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Task otherTask = (Task) other;
        if (!name.equals(otherTask.name)) {
            return false;
        }
        if (date == null && otherTask.date == null) {
            return true;
        }
        if (date == null ^ otherTask.date == null) {
            return false;
        }
        return date.equals(otherTask.date);
    }

    @Override
    public int hashCode() {
        int res = 1;

        res = PRIME_NUMBER * res + name.hashCode();
        res = PRIME_NUMBER * res + ((date == null) ? 0 : date.hashCode());

        return res;
    }

    public static class TaskBuilder {
        private String name;
        private LocalDate date;
        private LocalDate dueDate;
        private String description;

        public TaskBuilder(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name can not be null or blank");
            }
            this.name = name;
        }

        public boolean hasDate() {
            return date != null;
        }

        public boolean hasDueDate() {
            return dueDate != null;
        }

        public boolean hasDescription() {
            return description != null;
        }

        public TaskBuilder setDate(LocalDate date) throws TimeFrameMismatchException {
            if (date == null) {
                throw new IllegalArgumentException("date can not be null");
            }
            if (dueDate != null && date.isAfter(dueDate)) {
                throw new TimeFrameMismatchException("dueDate should be after the first date");
            }

            this.date = date;
            return this;
        }

        public TaskBuilder setDueDate(LocalDate dueDate) throws TimeFrameMismatchException {
            if (date == null) {
                throw new IllegalArgumentException("date can not be null");
            }
            if (dueDate != null && dueDate.isBefore(date)) {
                throw new TimeFrameMismatchException("dueDate should be after the first date");
            }

            this.dueDate = dueDate;
            return this;
        }

        public TaskBuilder setDescription(String description) {
            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException("description can not be null or blank");
            }

            this.description = description;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

}
