package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 저장소
 */
@Repository // @Component를 포함, 해당 클래스가 컴포넌트 스캔의 대상이 된다.
public class ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>();   // static
    private static long sequence = 0L;  // static

    // 상품 저장소에 상품을 저장한다.
    public Item save(Item item) {
        item.setId(++sequence); // ID 값 자동 증가(+1)
        store.put(item.getId(), item);

        return item;
    }

    // 상품 ID로 특정 상품의 정보를 조회한다.
    public Item findById(Long id) {
        return store.get(id);
    }

    // 전체 상품을 조회한다.
    public List<Item> findAll() {
        // 실제 store에 영향이 가지 않도록 ArrayList로 감싸서 반환한다.
        return new ArrayList<>(store.values());
    }

    // 상품 ID, 업데이트 파라미터로 특정 상품의 정보를 수정한다.
    public void update(Long id, Item updateParam) {
        // updateParam: 상품에 대한 수정 정보를 가지고 있다.

        // 수정할 상품을 찾아온다.
        Item findItem = findById(id);

        // 수정 정보를 반영한다.
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    // 상품 저장소 비우기 (test 용도)
    public void clearStore() {
        store.clear();
    }
}