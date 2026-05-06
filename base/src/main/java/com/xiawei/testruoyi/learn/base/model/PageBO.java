package com.xiawei.testruoyi.learn.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页业务对象（AppService 层使用）
 *
 * <p>用于应用服务层（AppService）与控制器层（Controller）之间的分页数据传输。</p>
 * <p>AppService 出参使用 PageBO<XxxBO>，Controller 负责将 PageBO 转换为 PageDTO。</p>
 *
 * @param <T> 数据类型（BO 类型）
 */
@Data
public class PageBO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private long current;

	/** 每页大小 */
	private long size;

	/** 总记录数 */
	private long total;

	/** 数据列表 */
	private List<T> records;

	/**
	 * 从 PageDO 构建 PageBO（消除手动 setter 转换）
	 *
	 * @param doPage  领域层分页对象
	 * @param records 已转换的记录列表（DO→BO）
	 * @param <S>    源记录类型（DO 类型）
	 * @param <T>    目标记录类型（BO 类型）
	 * @return PageBO 实例
	 */
	public static <S, T> PageBO<T> from(PageDO<S> doPage, List<T> records) {
		PageBO<T> bo = new PageBO<>();
		bo.setCurrent(doPage.getCurrent());
		bo.setSize(doPage.getSize());
		bo.setTotal(doPage.getTotal());
		bo.setRecords(records);
		return bo;
	}
}
