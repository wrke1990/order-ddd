package com.example.order.common.response;

import java.util.List;

/**
 * 分页响应类
 * @param <T> 数据类型
 */
public class PageResponse<T> extends CommonResponse<List<T>> {

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pages;

    private PageResponse() {
        super();
    }

    private PageResponse(int code, String message, List<T> data) {
        super(code, message, data);
    }

    private PageResponse(Integer pageNum, Integer pageSize, Long total, List<T> data) {
        super(200, "success", data);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = (int) Math.ceil((double) total / pageSize);
    }

    /**
     * 成功响应
     */
    public static <T> PageResponse<T> success(Integer pageNum, Integer pageSize, Long total, List<T> data) {
        return new PageResponse<>(pageNum, pageSize, total, data);
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PageResponse<?> that = (PageResponse<?>) o;

        if (pageNum != null ? !pageNum.equals(that.pageNum) : that.pageNum != null) return false;
        if (pageSize != null ? !pageSize.equals(that.pageSize) : that.pageSize != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        return pages != null ? pages.equals(that.pages) : that.pages == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pageNum != null ? pageNum.hashCode() : 0);
        result = 31 * result + (pageSize != null ? pageSize.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (pages != null ? pages.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pages=" + pages +
                ", code=" + getCode() +
                ", message='" + getMessage() + '\'' +
                ", data=" + getData() +
                '}';
    }
}
