package com.xiawei.testruoyi.learn.base.enums;

import lombok.Getter;

/**
 * 图片类型枚举
 */
@Getter
public enum ImgTypeEnum {

	PNG("png", "image/png"),
	JPG("jpg", "image/jpeg"),
	JPEG("jpeg", "image/jpeg"),
	GIF("gif", "image/gif"),
	BMP("bmp", "image/bmp"),
	WEBP("webp", "image/webp"),
	SVG("svg", "image/svg+xml"),
	UNKNOWN("unknown", "application/octet-stream");

	private final String ext;
	private final String mime;

	ImgTypeEnum(String ext, String mime) {
		this.ext = ext;
		this.mime = mime;
	}

	/**
	 * 根据文件扩展名获取图片类型
	 */
	public static ImgTypeEnum getByExt(String ext) {
		if (ext == null) {
			return UNKNOWN;
		}
		for (ImgTypeEnum type : values()) {
			if (type.ext.equalsIgnoreCase(ext)) {
				return type;
			}
		}
		return UNKNOWN;
	}

	/**
	 * 根据MIME类型获取图片类型
	 */
	public static ImgTypeEnum getByMime(String mime) {
		if (mime == null) {
			return UNKNOWN;
		}
		for (ImgTypeEnum type : values()) {
			if (type.mime.equalsIgnoreCase(mime)) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
