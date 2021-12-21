package com.shop.mall.service;

import com.shop.mall.dto.CartItemDto;
import com.shop.mall.entity.Cart;
import com.shop.mall.entity.CartItem;
import com.shop.mall.entity.Item;
import com.shop.mall.entity.Member;
import com.shop.mall.repository.CartItemRepository;
import com.shop.mall.repository.CartRepository;
import com.shop.mall.repository.ItemRepository;
import com.shop.mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMemberId(member.getId()).orElse(null);
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId()).orElse(null);
        if(savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }
}
