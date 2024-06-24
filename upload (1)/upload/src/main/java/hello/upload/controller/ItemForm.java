package hello.upload.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {
    //MultipartFile을 갖고 있음 Item과 다름
    private Long itemId;
    private String itemName;
    private List<MultipartFile> imagesFiles;
    private MultipartFile attachFile;
}
