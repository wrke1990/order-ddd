package com.example.order.infrastructure.repository.impl;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.repository.ShoppingCartRepository;
import com.example.order.infrastructure.assember.ShoppingCartAssembler;
import com.example.order.infrastructure.persistence.po.ShoppingCartPO;
import com.example.order.infrastructure.persistence.repository.JpaShoppingCartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 购物车仓储实现
 */
@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private final JpaShoppingCartRepository jpaShoppingCartRepository;
    private final ShoppingCartAssembler shoppingCartAssembler;

    public ShoppingCartRepositoryImpl(JpaShoppingCartRepository jpaShoppingCartRepository, ShoppingCartAssembler shoppingCartAssembler) {
        this.jpaShoppingCartRepository = jpaShoppingCartRepository;
        this.shoppingCartAssembler = shoppingCartAssembler;
    }

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        ShoppingCartPO shoppingCartPO = shoppingCartAssembler.toShoppingCartPO(shoppingCart);
        ShoppingCartPO savedPO = jpaShoppingCartRepository.save(shoppingCartPO);
        return shoppingCartAssembler.toShoppingCart(savedPO);
    }

    @Override
    public Optional<ShoppingCart> findByUserId(Long userId) {
        return jpaShoppingCartRepository.findByUserId(userId)
                .map(shoppingCartAssembler::toShoppingCart);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaShoppingCartRepository.deleteById(userId);
    }
}
