package com.example.order.domain.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果封装
 * 
 * @param <T> 数据类型
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> content;

    /**
     * 当前页码（从0开始）
     */
    private int pageNum;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private long totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否有上一页
     */
    private boolean hasPrevious;

    /**
     * 私有构造函数
     */
    private Page() {
    }

    /**
     * 创建分页查询结果
     * 
     * @param content        数据列表
     * @param pageNum        当前页码（从0开始）
     * @param pageSize       每页大小
     * @param totalElements  总记录数
     * @param <T>            数据类型
     * @return 分页查询结果
     */
    public static <T> Page<T> of(List<T> content, int pageNum, int pageSize, long totalElements) {
        Page<T> page = new Page<>();
        page.content = content;
        page.pageNum = pageNum;
        page.pageSize = pageSize;
        page.totalElements = totalElements;
        page.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        page.hasNext = pageNum < page.totalPages - 1;
        page.hasPrevious = pageNum > 0;
        return page;
    }

    // getter 方法
    public List<T> getContent() {
        return content;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                '}';
    }
}