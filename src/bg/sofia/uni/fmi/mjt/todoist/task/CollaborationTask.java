package bg.sofia.uni.fmi.mjt.todoist.task;

public class CollaborationTask extends Task {
    private String assigneeUsername;
    private static final String ASSIGNEE = "assignee";
    private static final String DASH = "- ";

    public CollaborationTask(CollaborationTaskBuilder builder) {
        super(builder);
        this.assigneeUsername = builder.assigneeUsername;
    }

    @Override
    public String toString() {
        StringBuilder collaborationTaskStr = new StringBuilder(super.toString());
        collaborationTaskStr.append(ASSIGNEE).append(DASH).append(assigneeUsername);
        return collaborationTaskStr.toString();
    }

    public void setAssigneeUsername(String assigneeUsername) {
        if (assigneeUsername == null || assigneeUsername.isBlank()) {
            throw new IllegalArgumentException("assigneeUsername can not be null or blank");
        }
        this.assigneeUsername = assigneeUsername;
    }

    public static class CollaborationTaskBuilder extends TaskBuilder {
        private String assigneeUsername;
        public CollaborationTaskBuilder(String name) {
            super(name);
        }

        public CollaborationTaskBuilder setAssigneeUsername(String assigneeUsername) {
            if (assigneeUsername == null || assigneeUsername.isBlank()) {
                throw new IllegalArgumentException("assigneeUsername can not be null or blank");
            }
            this.assigneeUsername = assigneeUsername;
            return this;
        }

        public CollaborationTask build() {
            return new CollaborationTask(this);
        }
    }
}
