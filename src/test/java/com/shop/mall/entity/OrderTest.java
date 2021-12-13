package com.shop.mall.entity;

import com.shop.mall.constant.ItemSellStatus;
import com.shop.mall.repository.ItemRepository;
import com.shop.mall.repository.MemberRepository;
import com.shop.mall.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        return Item.builder()
                .itemName("테스트 상품")
                .price(10000)
                .itemDetail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            order.getOrderItems().add(
                    OrderItem.builder()
                            .item(item)
                            .count(10)
                            .orderPrice(1000)
                            .order(order)
                            .build()
            );
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }

    public Order createOrder() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            order.getOrderItems().add(
                    OrderItem.builder()
                            .item(item)
                            .count(10)
                            .orderPrice(1000)
                            .order(order)
                            .build()
            );
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }
}