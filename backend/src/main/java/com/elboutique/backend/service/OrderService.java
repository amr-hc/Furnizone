package com.elboutique.backend.service;

import com.elboutique.backend.DTO.response.OrderResponse;
import com.elboutique.backend.model.Cart;
import com.elboutique.backend.model.Order;
import com.elboutique.backend.model.OrderProduct;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.model.User;
import com.elboutique.backend.repository.CartRepository;
import com.elboutique.backend.repository.OrderRepository;
import com.elboutique.backend.repository.ProductRepository;
import com.elboutique.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getUserOrders(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderRepository.findByUserAndStatusNot(user, Order.Status.CANCEL.toString()).stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Order createOrder(Integer userId) {

        List<Cart> carts  = cartRepository.findByUserId(userId);
        if (carts.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        Order order = new Order();
        order.setUser(User.builder().id(userId).build());
        order.setStatus(Order.Status.PROGRESS);

        List<OrderProduct> orderProducts = carts.stream()
            .map(productInCart -> {
                Product product = productRepository.findById(productInCart.getProduct().getId()).get();
                if (productInCart.getQuantity() > product.getStock()) {
                    throw new IllegalArgumentException("Not enough stock for product: " + product.getTitle());
                }
                product.setStock(product.getStock() - productInCart.getQuantity());
                productRepository.save(product);
                return OrderProduct.builder()
                        .order(order)
                        .product(product)
                        .quantity(productInCart.getQuantity())
                        .price(product.getPrice())
                        .build();
            }).collect(Collectors.toList());

        cartRepository.deleteByUserId(userId);
        order.setOrderProducts(orderProducts);
        return orderRepository.save(order);
    }


    public OrderResponse getOrderById(Integer id, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderResponse.fromEntity(order);
    }

    public Order updateOrder(Integer id, Order updatedOrder) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(updatedOrder.getStatus());
        return orderRepository.save(order);
    }

    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderRepository.delete(order);
    }

    @Transactional
    public Order cancelOrder(Integer id, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!"progress".equals(order.getStatus())) {
            throw new IllegalArgumentException("Cannot cancel order in current state");
        }

        order.setStatus(Order.Status.CANCEL);
        orderRepository.save(order);

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            product.setStock(product.getStock() + orderProduct.getQuantity());
            productRepository.save(product);
        }

        return order;
    }

    public Order markOrderAsDone(Integer id, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!"progress".equals(order.getStatus())) {
            throw new IllegalArgumentException("Order cannot be marked as done");
        }

        order.setStatus(Order.Status.DONE);
        return orderRepository.save(order);
    }
}
