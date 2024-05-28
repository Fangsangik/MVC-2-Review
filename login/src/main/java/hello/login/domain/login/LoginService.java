package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String logInId, String password) {
        return memberRepository.findByLogInId(logInId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

//        Optional<Member> findMemberOptional = memberRepository.findByLogInId(logInId);
//        Member member = findMemberOptional.get();
//        if (member.getPassword().equals(password)){
//            return member;
//        } else {
//            return null;
//        }
    }
}
