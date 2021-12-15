package com.shop.mall.entity;

import com.shop.mall.constant.ItemSellStatus;
import com.shop.mall.dto.ItemFormDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}
