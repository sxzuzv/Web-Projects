package hello.itemservice.controller;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class ItemServiceController {
    // @RequiredArgsConstructor: 기본 생성자 자동 생성 및 생성자 주입
    private final ItemRepository itemRepository;

    // 상품 목록 출력
    @GetMapping
    public String item(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}