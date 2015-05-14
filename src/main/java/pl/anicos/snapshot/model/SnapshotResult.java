package pl.anicos.snapshot.model;

public class SnapshotResult {
	// base64
	private String thumb;

	public SnapshotResult() {	
	}
	
	public SnapshotResult(String thumb) {
		this.thumb = thumb;
	}

	public String getThumb() {
		return thumb;
	}

}
