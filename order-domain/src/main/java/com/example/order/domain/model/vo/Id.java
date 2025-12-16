package com.example.order.domain.model.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * ID值对象
 * 统一处理各种实体的ID，避免原生类型的滥用
 */
public class Id implements Serializable {

    private final Long value;

    /**
     * 私有构造函数，防止外部直接创建
     */
    private Id(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ID值必须大于0");
        }
        this.value = value;
    }

    /**
     * 创建ID实例
     */
    public static Id of(Long value) {
        return new Id(value);
    }

    /**
     * 创建ID实例（字符串形式）
     */
    public static Id of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ID字符串不能为空");
        }
        try {
            return new Id(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID字符串必须是有效的数字");
        }
    }

    /**
     * 获取ID值
     */
    public Long getValue() {
        return value;
    }

    /**
     * 转换为字符串
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * 相等性比较
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id = (Id) o;
        return Objects.equals(value, id.value);
    }

    /**
     * 哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}