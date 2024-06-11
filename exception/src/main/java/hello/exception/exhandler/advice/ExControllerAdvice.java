package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice //RestController + ResponseBody
public class ExControllerAdvice {
    //여러 컨트롤러에서 발생한 오류들을 여기서 한번에 처리

    @ResponseStatus(HttpStatus.BAD_REQUEST) //200으로 안하고 다른 HttpStatus로 변경
    @ExceptionHandler(IllegalArgumentException.class) //Controller에서 예외가 터지면 여기서 잡고
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        //여기서 예외 터짐
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage()); //그대로 JSON 반환

        //Controller에 있는 Exception 찾아서 호출 -> log 찍고 -> 정상흐름으로 변경해서
        //정상적으로 return -> Http 상태 코드가 200이 됨

        //여기서 흐름이 모두 끝난 상태
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-Ex", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    //Controller 안에서 발생한 오류만 처리
    //모든 예외를 처리 해준다. (실수로 놓친 예외 등등을 처리)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //200 -> 500으로 상태 코드 변경
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
