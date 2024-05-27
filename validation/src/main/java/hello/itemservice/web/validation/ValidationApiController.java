package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController //자동으로 ResponseBody 붙는다.
@RequestMapping("/validation/api/items")
public class ValidationApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
        log.info("API 호출");

        if (bindingResult.hasErrors()){
            log.info("검증 오류 발생 errors = {}", bindingResult);
            return bindingResult.getAllErrors(); //모든 오류 반환
        }

        log.info("성공 로직 실행");
        return form;

        /*
        error 잘못된 값 요청시 Controller 호출 x
        JSON form이 객체로 변경되어야 validation 가능한데, 이 단계 까지 못감

        HTTP 요청 파라미터 처리 -> modelAttribute 필드 단위 세밀히 적용
        HttpMessageConverter -> @ModelAttribute와 다르게 각 다른 필드 단위로 적용 되는 것 X
        전체 객체 단위 적용

        @ModelAttribute -> 필드 단위
        @RequestBody -> JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체 진행 X, 예외 발생
         */
    }
}
