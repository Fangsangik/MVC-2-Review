package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/v1")
    public String helloV1(
            HttpServletRequest request
    ) {
        //HTTP 요청 파라미터는 모두 문자 처리
        String data = request.getParameter("data");
        Integer val = Integer.valueOf(data);
        System.out.println("val = " + val);
        return "ok";
    }

    @GetMapping("/v2")
    public String helloV2(
            @RequestParam Integer data
    ) {
        System.out.println("data = " + data);
        return "ok";
        //HTTP 쿼리 스트링으로 전달 하는 data는 숫자가 아니라 문자
        //스프링이 중간에서 숫자로 변경
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort.getIp() = " + ipPort.getIp());
        System.out.println("ipPort.getPort() = " + ipPort.getPort());

        return "ok";
    }
}
