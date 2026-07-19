package level_3.Metadata;

import java.util.UUID;

public abstract class Constraint {
    private UUID constraintId;
    private String constraintName;
    private boolean enabled;

    public Constraint() {
        this.constraintId = UUID.randomUUID();
        this.enabled = true;
    }

    public Constraint(String constraintName) {
        this();
        this.constraintName = constraintName;
    }

    public boolean validate() {
        return enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
