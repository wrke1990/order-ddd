package com.example.order.infrastructure.persistence.repository;

import com.example.order.infrastructure.persistence.po.ShoppingCartPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA购物车仓库接口
 */
@Repository
public interface JpaShoppingCartRepository extends JpaRepository<ShoppingCartPO, Long> {

    /**
     * 根据用户ID查询购物车
     */
    Optional<ShoppingCartPO> findByUserId(Long userId);
}
