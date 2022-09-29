package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {
    //싱글톤이어서 여러 명이 동시에 접근이 가능해서 컨커런트 해쉬맵 쓰기
    private static final Map<Long,Item> store = new ConcurrentHashMap<>();
    private static long sequence = 0L; // static

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item>findAll(){
        // map의 values 쓰고 생성자에 넣으면 자동으로 어레이리스트에 들어감
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam){
        Item item = findById(itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    public void cleatStore(){
        store.clear();
    }
}
