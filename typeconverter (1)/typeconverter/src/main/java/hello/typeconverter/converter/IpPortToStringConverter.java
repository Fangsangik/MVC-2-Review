package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {
    @Override
    public String convert(IpPort source) {
        //IP port 객체를 문자로 반환
      log.info("convert source ={}", source);
      return source.getIp() + ":" + source.getPort();
    }
}
