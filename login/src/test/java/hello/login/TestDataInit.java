package hello.login;

import hello.login.domain.item.Item;
import hello.login.domain.item.ItemRepository;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public TestDataInit(ItemRepository itemRepository, MemberRepository memberRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 1000, 10));
        itemRepository.save(new Item("itemB", 2000, 20));

        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("123");
        member.setName("테스터");
        memberRepository.save(member);
    }
}

