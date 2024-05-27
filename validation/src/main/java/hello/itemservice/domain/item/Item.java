package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price*_this.quantity >= 10000")
//제약이 많고 복잡해서 추천 X
public class Item {

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank (groups = {SaveCheck.class, UpdateCheck.class})
    //(message = "공백 X")//빈값 + 공백만 있는 경우 허용 X
    private String itemName;

//    @NotNull (groups = {SaveCheck.class, UpdateCheck.class})
//    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

//    @NotNull (groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999, groups = SaveCheck.class)
    //@Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    /*
    groups 기능을 사용하면 -> 등록 수정시 각각 다르게 검증
    단 복잡도 올라감
     */
}
