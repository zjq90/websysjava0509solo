package com.flower.service;

import com.flower.entity.Cart;
import com.flower.entity.Product;
import com.flower.repository.CartRepository;
import com.flower.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 购物车服务类
 */
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Cart> getCartList(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setPrice(product.getPrice());
            return cartRepository.save(cart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            return cartRepository.save(cart);
        }
    }

    @Transactional
    public Cart updateCartQuantity(Long userId, Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));

        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));

        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        cartRepository.delete(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}