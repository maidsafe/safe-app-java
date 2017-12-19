package net.maidsafe.safe_app;

/// Represents the FFI-safe account info.
public class AccountInfo {
    private long mutationsDone;
    private long mutationsAvailable;

    public long getMutationsDone() {
        return mutationsDone;
    }

    public void setMutationsDone(final long val) {
        mutationsDone = val;
    }

    public long getMutationsAvailable() {
        return mutationsAvailable;
    }

    public void setMutationsAvailable(final long val) {
        mutationsAvailable = val;
    }

}

