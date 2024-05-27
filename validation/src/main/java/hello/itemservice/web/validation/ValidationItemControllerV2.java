package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";

        //빈 데이터를 넣어서 모델로 보여줌
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item,
                          BindingResult bindingResult,
                            //무언가 바인딩 된 결과 & 순서 중요 ModelAttribute 다음에 bindingResult 들어가야 한다.
                          RedirectAttributes redirectAttributes) {


        //검증 오류시
        if (!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
        bindingResult.addError(new FieldError("item", "price", "가격은 1000 ~ 1000000까지 허용"));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9999까지 허용"));
        }

        if (item.getPrice() != null && item.getPrice() != null){
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10000원 이상이어야 합니다" + rstPrice));
            }
        }

        if (bindingResult.hasErrors()){
            log.info("errors  = " ,bindingResult );
            return "validation/v2/addForm";
        }

        //modelAttribute에는 안담아 줘도 된다. -> bindingResult에는 같이 model이 넘어간다.

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";

        //item에 데이터 들어오고, 들어온 데이터로, model에 자동으로 item 넣어줌
        //다시 addForm 들어감 -> item에서 전달 된 값이 그대로 재사용
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(item.getItemName())){
            //item.getItemName() -> rejectedValue (거절 된 값) -> 옳바르진 않은 값을 넣었을때 값 유지
            //bindingFailure -> 바인딩 자체 실패인가??

            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1000 ~ 1000000까지 허용"));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9999까지 허용"));
        }

        if (item.getPrice() != null && item.getPrice() != null){
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10000원 이상이어야 합니다" + rstPrice));
            }
        }

        if (bindingResult.hasErrors()){
            log.info("errors  = " ,bindingResult );
            return "validation/v2/addForm";
        }

        //modelAttribute에는 안담아 줘도 된다. -> bindingResult에는 같이 model이 넘어간다.

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        if (!StringUtils.hasText(item.getItemName())){
            //item.getItemName() -> rejectedValue (거절 된 값) -> 옳바르진 않은 값을 넣었을때 값 유지
            //bindingFailure -> 바인딩 자체 실패인가??

            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        if (item.getPrice() != null && item.getPrice() != null){
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, rstPrice}, null));
            }
        }

        if (bindingResult.hasErrors()){
            log.info("errors  = " ,bindingResult );
            return "validation/v2/addForm";
        }

        //modelAttribute에는 안담아 줘도 된다. -> bindingResult에는 같이 model이 넘어간다.

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        if (!StringUtils.hasText(item.getItemName())){
            //item.getItemName() -> rejectedValue (거절 된 값) -> 옳바르진 않은 값을 넣었을때 값 유지
            //bindingFailure -> 바인딩 자체 실패인가??

            bindingResult.rejectValue("itemName", "required");
            //errocode = required.xxx
            //field명

            //ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue( "quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getPrice() != null){
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                bindingResult.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }

        if (bindingResult.hasErrors()){
            log.info("errors  = " ,bindingResult );
            return "validation/v2/addForm";
        }

        //modelAttribute에는 안담아 줘도 된다. -> bindingResult에는 같이 model이 넘어간다.

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        itemValidator.validate(item, bindingResult); //검증기 때리면 erros로 다 값이 나온다.

        if (bindingResult.hasErrors()) {
            log.info("errors = {}" , bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")

    //@Validated -> 검증기 편하게 적용 가능
    public String addV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        itemValidator.validate(item, bindingResult); //검증기 때리면 erros로 다 값이 나온다.

        if (bindingResult.hasErrors()) {
            log.info("errors = {}" , bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}" , dataBinder);

        //이전에 만들어 둔 validator를 넣어준다.
        //항상 호출 될떄 어디서든 검증기를 맞게 적용 할 수 있다.
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

