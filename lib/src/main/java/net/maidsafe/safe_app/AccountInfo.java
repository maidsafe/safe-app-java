package net.maidsafe.safe_app;

/// Represents the FFI-safe account info.
public class AccountInfo {
	private long mutationsDone;
	private long mutationsAvailable;

	public AccountInfo() {

	}
	public AccountInfo(long mutationsDone, long mutationsAvailable) {
		this.mutationsDone = mutationsDone;
		this.mutationsAvailable = mutationsAvailable;
	}
	public long getMutationsDone() {
		return mutationsDone;
	}

	public void setMutationsDone(final long val) {
		this.mutationsDone = val;
	}

	public long getMutationsAvailable() {
		return mutationsAvailable;
	}

	public void setMutationsAvailable(final long val) {
		this.mutationsAvailable = val;
	}

}

