package com.shop.mall.service;

import com.shop.mall.constant.ItemSellStatus;
import com.shop.mall.dto.ItemFormDto;
import com.shop.mall.entity.Item;
import com.shop.mall.entity.ItemImg;
import com.shop.mall.repository.ItemImgRepository;
import com.shop.mall.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    ItemRepository itemRepository;

    /**
     * MockMultipart 클래스를 이용해 가짜 MultipartFile 리스트를 만들어 반환
     * @return
     * @throws Exception
     */
    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 사상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertThat(itemFormDto.getItemName()).isEqualTo(item.getItemName());
        assertThat(itemFormDto.getItemSellStatus()).isEqualTo(item.getItemSellStatus());
        assertThat(itemFormDto.getItemDetail()).isEqualTo(item.getItemDetail());
        assertThat(itemFormDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(itemFormDto.getStockNumber()).isEqualTo(item.getStockNumber());
        assertThat(multipartFileList.get(0).getOriginalFilename()).isEqualTo(itemImgList.get(0).getOriImgName());
    }
}