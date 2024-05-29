package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";

        //리다이렉트 해서 items로 이동
    }

    //@GetMapping("/")
    public String homeLogin(
            // @CookieValue(name = "memberId", required = false)
            // @CookieValue -> 쿠키 편하게 가져온다.
            // required = false => login 하지 않은 사용자도 들어와야 한다.
            @CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        //성공 로직
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(
            HttpServletRequest request, Model model) {

        //세션 관리자에 저장된 회원 정보를 조회
        Member member = (Member) sessionManager.getSession(request);

        //로그인
        if (member == null) {
            return "home";
        }

        //성공 로직
        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //로그인
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    //Controller에서 공통으로 사용하는 것들을 annotation 하나로 간단하게
    public String homeLoginV3ArgumentResolver(
            @Login Member loginMember, Model model) {

        //로그인
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3(
            HttpServletRequest request, Model model) {

        //로그인 하지 않은 사용자 -> session이 생성되는 경우가 있음
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember= (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //로그인
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}