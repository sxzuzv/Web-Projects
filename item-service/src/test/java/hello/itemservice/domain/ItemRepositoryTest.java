package hello.itemservice.domain;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ItemRepositoryTest {  // 최근 JUnit의 경우, public 생략 가능
    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        // 테스트 실행이 완료될 때마다 상품 저장소 clear!
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);

        //then: 찾은 상품과 저장한 상품이 동일한가?
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }


    @Test
    void findAll() {
        //given
        Item itemA = new Item("itemA", 10000, 10);
        Item itemB = new Item("itemB", 20000, 20);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        //when
        List<Item> result = itemRepository.findAll();

        //then: 조회 목록의 사이즈 비교 및 목록에 지정한 상품이 존재하는가?
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(itemA, itemB);
    }
}