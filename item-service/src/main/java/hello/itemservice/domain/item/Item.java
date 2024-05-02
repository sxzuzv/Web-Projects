package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

/**
 * 상품 객체
 */
@Getter @Setter // @Data 사용 주의
public class Item {
    private Long id;            // ID
    private String itemName;    // 상품명
    private Integer price;      // 상품 가격
    private Integer quantity;   // 상품 수량

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}