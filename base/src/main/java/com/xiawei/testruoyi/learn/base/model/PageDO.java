package com.xiawei.testruoyi.learn.base.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页领域对象（DomainService 层使用）
 *
 * <p>用于领域服务层（DomainService）与应用服务层（AppService）之间的分页数据传输。</p>
 * <p>DomainService 出参使用 PageDO<XxxDO>，AppService 负责将 PageDO 转换为 PageBO。</p>
 *
 * @param <T> 数据类型（DO 类型）
 */
@Data
public class PageDO<T> implements Serializable {

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
	 * 从 MyBatis-Plus Page 构建 PageDO（消除手动 setter 转换）
	 *
	 * @param poPage  MyBatis-Plus 分页对象
	 * @param records 已转换的记录列表（PO→DO）
	 * @param <S>    源记录类型（PO 类型）
	 * @param <T>    目标记录类型（DO 类型）
	 * @return PageDO 实例
	 */
	public static <S, T> PageDO<T> from(Page<S> poPage, List<T> records) {
		PageDO<T> result = new PageDO<>();
		result.setCurrent(poPage.getCurrent());
		result.setSize(poPage.getSize());
		result.setTotal(poPage.getTotal());
		result.setRecords(records);
		return result;
	}
}
