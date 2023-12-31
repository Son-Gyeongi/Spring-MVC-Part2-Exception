package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import hello.exception.resolver.MyHandlerExceptionResolver;
import hello.exception.resolver.UserHandlerExceptionResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // WebMvcConfigurer 는 인터셉터 때 필요

    // 서블릿 예외 처리 - LogFilter 필터 등록
//    @Bean // 스프링 빈으로 등록, 인터셉터 사용으로 logFilter() 적용되면 안되서 빈에서 제외
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*"); // 전체 경로
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        // 해당 필터는 REQUEST, ERROR 두 가지의 경우에 호출이 된다.
        return filterRegistrationBean;
    }

    // 서블릿 예외 처리 - LogInterceptor 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") // 전체 경로 허용
                .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**"); // 제외 경로
        // 인터셉터는 필터처럼 setDispatcherTypes() 할 수 없다.
        // 대신에 강력한 excludePathPatterns()이 있다. 그래서 그냥 경로를 뺴면 된다.
        // 예시 "/error-page/**" 오루 페리지 경로 관련된 거 빼기
    }

    // API 예외 처리 - HandlerExceptionResolver 시작
    // MyHandlerExceptionResolver 등록
    /**
     * 기본 설정을 유지하면서 추가
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());

        /**
         * API 예외 처리 - HandlerExceptionResolver 활용
         * UserHandlerExceptionResolver 등록
         */
        resolvers.add(new UserHandlerExceptionResolver());
    }
}
