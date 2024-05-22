package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    //item을 검증
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //파라미터로 넘어오는 class가 Item과 같냐?
        //item == clazz
    }

    @Override
    public void validate(Object target, Errors errors) {
        //Object target & bindingResult 넣을 수 있다.
        Item item = (Item) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue( "quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getPrice() != null){
            int rstPrice = item.getPrice() * item.getQuantity();
            if (rstPrice < 10000) {
                //특정 필드에 대한 오류가 아닌 global Error
                errors.reject("totalPriceMin", new Object[]{10000, rstPrice}, null);
            }
        }
    }
}
