package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ConverterTest {
    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer rst = converter.convert("10");
        assertThat(rst).isEqualTo(10);
    }

    @Test
    void IntegerToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String rst = converter.convert(10);
        assertThat(rst).isEqualTo("10");
    }

    @Test
    void stringToIpPort(){
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String source = "127.0.0.1:8080";
        IpPort rst = converter.convert(source);
        //equalsHashCode -> 참조값이 달라도 data가 같으면 true
        assertThat(rst).isEqualTo(new IpPort("127.0.0.1", 8080));
    }

    @Test
    void ipToString(){
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);
        String rst = converter.convert(source);
        assertThat(rst).isEqualTo("127.0.0.1:8080");
    }
}
