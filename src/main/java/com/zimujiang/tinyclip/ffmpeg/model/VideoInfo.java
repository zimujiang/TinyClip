package com.zimujiang.tinyclip.ffmpeg.model;

/**
 * Created by tonydeng on 15/4/16.
 */
public class VideoInfo {
	private String format;
	private long size;
	private long duration;
	private String duration_f;
	private VideoResolution resolution;
	private int rotate;

	public VideoInfo() {
	}

	public VideoInfo(long size) {
		setSize(size);
	}

	public String getDuration_f() {
		return duration_f;
	}

	public void setDuration_f(String duration_f) {
		this.duration_f = duration_f;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public VideoResolution getResolution() {
		return resolution;
	}

	public void setResolution(VideoResolution resolution) {
		this.resolution = resolution;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	@Override
	public String toString() {
		return "VideoInfo{" + "format='" + format + '\'' + ", size=" + size + ", duration=" + duration + ", duration_f="
				+ duration_f + ", resolution=" + resolution + ", rotate=" + rotate + '}';
	}
}
