package com.sparta.week04.utils;

import com.sparta.week04.domain.ItemDto;
import com.sparta.week04.domain.Product;
import com.sparta.week04.repository.ProductRepository;
import com.sparta.week04.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final NaverShopSearch naverShopSearch;

    @Scheduled(cron = "0 0 1 * * *")
    public void updatePrice() throws InterruptedException{
        System.out.println("가격 업데이트 실행");
        List<Product> productList = productRepository.findAll();
        for(int i=0;i<productList.size();i++){
            TimeUnit.SECONDS.sleep(1);
            Product p = productList.get(i);
            String title = p.getTitle();
            String resultString = naverShopSearch.search(title);
            List<ItemDto> itemDtoList = naverShopSearch.fromJSONtoItems(resultString);
            ItemDto itemDto = itemDtoList.get(0);
            Long id = p.getId();
            productService.updateBySearch(id, itemDto);
        }
    }
}
