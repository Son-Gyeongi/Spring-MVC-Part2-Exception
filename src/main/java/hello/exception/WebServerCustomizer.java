package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// 서블릿 예외 처리 - 오류 화면 제공
//@Component // 스프링에 등록, 스프링 부트가 제공하는 기본 오류 메커니즘(new ErrorPage("/error"))을 사용하도록 주석 처리하자.
@Component // API 예외 처리 - 시작 (서블릿 오류 페이지 방식을 사용)
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        // HTTP 상태 코드
        // 404. NOT_FOUND 에러가 나면 /error-page/400 페이지를 호출해라.
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        // Exception 발생 했을 때
        // RuntimeException 자식 예외들까지 포함
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        // 등록
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
