package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";

        //빈 데이터를 넣어서 모델로 보여줌
    }

    @PostMapping("/add")
    //@Validated -> 검증기 편하게 적용 가능
    public String addItemV2(@Validated @ModelAttribute("item") ItemSaveForm itemSaveForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //@ModelAttribute ("itemSaveForm") -> view Template 변경 X, 명명을 따로 지정 X -> itemSaveForm
        //model.addAtt... -> itemSaveForm 이렇게 담긴다. 

        if (itemSaveForm.getPrice() != null && itemSaveForm.getPrice() != null) {
            int rstPrice = itemSaveForm.getPrice() * itemSaveForm.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v4/addForm";
        }

        //성공로직
        Item item = new Item();
        item.setItemName(item.getItemName());
        item.setPrice(item.getPrice());
        item.setQuantity(item.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(
            @PathVariable Long itemId,
            @Validated @ModelAttribute("item") ItemUpdateForm itemUpdateForm,
            BindingResult bindingResult) {

        if (itemUpdateForm.getPrice() != null && itemUpdateForm.getPrice() != null) {
            int rstPrice = itemUpdateForm.getPrice() * itemUpdateForm.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        Item itemParam = new Item();
        itemParam.setItemName(itemUpdateForm.getItemName());
        itemParam.setPrice(itemUpdateForm.getPrice());
        itemParam.setQuantity(itemUpdateForm.getQuantity());

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm";
        }


        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }
}

