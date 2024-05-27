package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";

        //빈 데이터를 넣어서 모델로 보여줌
    }


    //@PostMapping("/add")
    //@Validated -> 검증기 편하게 적용 가능
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //Validated -> 그냥 BeanValidation이 적용이 된다 -> 라이브러리 적용하면 자동으로 등록
        //NotNull과 같은 annotation을 보고 자동으로 등록 = 글로벌 validator ->
        //@Valid(자바 표준 검증 애노테이션), @Validated(스프링 전용 검증 애노테이션)만 적용하면 된다.


        if (item.getPrice() != null && item.getPrice() != null) {
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    /*
    검증 순서
    @ModelAttribute -> requestParam을 스프링 프레임 워크가 딱딱 넣어줌

    실패시 -> typeMissMatch로 FieldError를 추가
    바인딩에 실패한 필드는 BeanValidation 적용x
    타입 변환에 성공 => 바인딩에 성공한 필드여야 BeamValidation 적용 의미
     */

    @PostMapping("/add")
    //@Validated -> 검증기 편하게 적용 가능
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //@Validated(SaveCheck.class) -> Validated가 먹힐때 SaveCheck인 것만 적용

        if (item.getPrice() != null && item.getPrice() != null) {
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit(
            @PathVariable Long itemId,
            @Validated @ModelAttribute Item item,
            BindingResult bindingResult) {

        if (item.getPrice() != null && item.getPrice() != null) {
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }


        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(
            @PathVariable Long itemId,
            @Validated (UpdateCheck.class) @ModelAttribute Item item,
            BindingResult bindingResult) {

        if (item.getPrice() != null && item.getPrice() != null) {
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }


        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}

