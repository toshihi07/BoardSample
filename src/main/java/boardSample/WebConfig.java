package boardSample;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
      PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
      //ページ単位に表示する件数
      resolver.setFallbackPageable(new PageRequest(0, 5, Direction.ASC);
      argumentResolvers.add(resolver);
      super.addArgumentResolvers(argumentResolvers);
  }

}