package hello.itemservice.controller;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 상품 등록 Form 출력
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // 입력된 상품 정보 등록
    // @PostMapping("/add")
    public String saveV1(@RequestParam("itemName") String itemName,
                       @RequestParam("price") int price,
                       @RequestParam("quantity") Integer quantity,
                       Model model) {

        // Form에 입력된 데이터는 name 속성에 지정해둔 이름으로 넘어온다.
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        // 상품 상세 페이지로 이동한다.
        return "basic/item";
    }

    // @PostMapping("/add")
    public String saveV2(@ModelAttribute("item") Item item) {
        /* @ModelAttribute 사용 시, 아래의 코드를 자동 처리해준다.
            Item item = new Item();
            item.setItemName(itemName);
            item.setPrice(price);
            item.setQuantity(quantity);
         */

        itemRepository.save(item);

        // 또한, ("item") 설정해둔 이름으로 모델에 자동으로 데이터를 넣어준다.
        // 메서드의 매개변수로 모델 객체를 받을 필요도 없다!
        // model.addAttribute("item", item);

        return "basic/item";
    }

    // @PostMapping("/add")
    public String saveV3(@ModelAttribute Item item) {
        itemRepository.save(item);

        // @ModelAttribute의 name 속성은 생략할 수 있다.
        // 이 경우, 매개변수로 전달받은 객체의 클래스 이름을 활용해 모델에 데이터를 넘긴다.
        // => 클래스 이름 Item의 첫 글자만 소문자로 변경(item)하여 이름으로 지정한다.
        // model.addAttribute("item", item);

        return "basic/item";
    }

    // @PostMapping("/add")
    public String saveV4(Item item) {
        // @ModelAttribute 애너테이션 생략이 가능하다.
        // 매개변수로 임의의 객체를 받는 경우, @ModelAttribute가 자동으로 적용된다.
        // 모델에 데이터를 담을 때에는 V3와 동일한 과정을 거친다.

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * PRG - POST Redirect GET
     */
    // @PostMapping("/add")
    public String saveV5(Item item) {
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * RedirectAttributes: URL 인코딩, pathVariable, 쿼리 파라미터까지 처리해준다.
     */
    @PostMapping("/add")
    public String saveV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        // {itemId}: RedirectAttributes에 넣은 itemId를 사용한다.
        // status: 쿼리 파라미터 형식으로 URL에 붙는다.
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    // 상품 상세 정보 확인
    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 상품 수정 Form 출력
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    // 상품 수정 반영
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        // 수정 정보를 @ModelAttribute 애너테이션을 사용해 Item 객체 형태로 받은 후 반영한다.
        itemRepository.update(itemId, item);

        // redirect로 상품 상세 화면으로 넘어간다.
        // {itemId}: @PathVariable을 사용해 받은 itemId를 그대로 사용한다.
        return "redirect:/basic/items/{itemId}";
    }
}