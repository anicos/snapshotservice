package pl.anicos.snapshot.model;

public class SnapshotDetail {

	private final String url;
	private final int thumbnailWidth;
	private final int thumbnailHeight;
	private final int windowWidth;
	private final int windowHeight;

	public SnapshotDetail(String url, int thumbnailWidth, int thumbnailHeight, int windowWidth, int windowHeight) {
		this.url = url;
		this.thumbnailWidth = thumbnailWidth;
		this.thumbnailHeight = thumbnailHeight;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	public String getUrl() {
		return url;
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

}
