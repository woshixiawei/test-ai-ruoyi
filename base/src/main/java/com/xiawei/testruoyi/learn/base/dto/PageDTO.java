package com.xiawei.testruoyi.learn.base.dto;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应DTO
 *
 * @param <T> 数据类型
 */
@Data
public class PageDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前页码
	 */
	private long current;

	/**
	 * 每页大小
	 */
	private long size;

	/**
	 * 总记录数
	 */
	private long total;

	/**
	 * 数据列表
	 */
	private List<T> records;

	/**
	 * 从 PageBO 构建 PageDTO（消除手动 setter 转换）
	 *
	 * @param boPage  应用层分页对象
	 * @param records 已转换的记录列表（BO→DTO）
	 * @param <S>    源记录类型（BO 类型）
	 * @param <T>    目标记录类型（DTO 类型）
	 * @return PageDTO 实例
	 */
	public static <S, T> PageDTO<T> from(PageBO<S> boPage, List<T> records) {
		PageDTO<T> dto = new PageDTO<>();
		dto.setCurrent(boPage.getCurrent());
		dto.setSize(boPage.getSize());
		dto.setTotal(boPage.getTotal());
		dto.setRecords(records);
		return dto;
	}
}
