package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";// 템플릿 디렉토리로 이동
    }
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }
    // Get으로 오면 addForm을 호출
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }
    // Post로 오면 실제로 저장
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){
        Item item = new Item(itemName,price,quantity);
        itemRepository.save(item);
        model.addAttribute("item",item);
        return "basic/item";
    }

    //@PostMapping("/add") //모델 어트리뷰트가 자동으로 만들어줌
    public String addItemV2(@ModelAttribute("Item") Item item, Model model){
        itemRepository.save(item);
        //model.addAttribute("item",item); // 자동 추가, 생략 가능
        return "basic/item";
    }

    //@PostMapping("/add") //모델 어트리뷰트에 이름이 지정되지 않으면 클래스 명의 앞 글자를 소문자로 바꾼 걸로 넘어간다.
    public String addItemV3(@ModelAttribute Item item){
        itemRepository.save(item);
        //model.addAttribute("item",item); // 자동 추가, 생략 가능
        return "basic/item";
    }

    //@PostMapping("/add") //모델 어트리뷰트 어노테이션 생략 가능
    public String addItemV4(Item item){
        itemRepository.save(item);
        //model.addAttribute("item",item); // 자동 추가, 생략 가능
        return "basic/item";
    }
    // PRG 패턴 -> Post/Redirect/Get 패턴
    // 중요한 정보를 수정하거나 저장할 때 중복을 막는 패턴이다.
    //@PostMapping("/add") //모델 어트리뷰트 어노테이션 생략 가능
    public String addItemV5(Item item){
        itemRepository.save(item);
        //model.addAttribute("item",item); // 자동 추가, 생략 가능
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add") //모델 어트리뷰트 어노테이션 생략 가능
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
        //model.addAttribute("item",item); // 자동 추가, 생략 가능
        // status는 쿼리 파라미터 형식으로 들어가게 됨
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        System.out.println("들어옴");
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId,item);
        //스프링에서 리다이렉트 하는 법
        return "redirect:/basic/items/{itemId}";
    }



    //서버 시작될 때 먼저 실행 되는 것
    //테스트용 데이터를 추가했습니다.
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));

    }
}
