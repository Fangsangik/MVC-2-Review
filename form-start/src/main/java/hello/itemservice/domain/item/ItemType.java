package hello.itemservice.domain.item;

import org.springframework.web.bind.annotation.GetMapping;

public enum ItemType {
    BOOK("책"), FOOD("음식"), ETC("기타");

    private final String description;

    ItemType(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
