package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// API 예외 처리 - HandlerExceptionResolver 시작
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Exception ex 이 넘어왔으면 정상적인 ModelAndView로 반환해주면 된다.\

        // 500에러인 http://localhost:8080/api/members/ex로 들어와도 resolveException()는 호출된다.
        log.info("call resolver", ex); // Exception의 경우  '= {}' 필요없다.

        try {
            if (ex instanceof IllegalArgumentException) { // 예외가 IllegalArgumentException 이면
                log.info("IllegalArgumentException resolver to 400");
                // 서버 내부에서 IllegalArgumentException가 발생해서 컨트롤러 밖으로 나오면 400 오류로 바꿀거다.
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); // ex 예외를 여기서 먹어버린다.
                return new ModelAndView(); // 새로운 ModelAndView를 반환해준다.
                // 예외는 먹어버리고
                // new ModelAndView();를 빈값으로 반환하면 정상적인 흐름으로 return 되어서
                // 서블릿 컨테이너 WAS까지 정상적으로 흐름이 반환된다. 결국 서블릿 컨테이너는 400으로 왔네라고 인식한다.
            }
        } catch (IOException e) {
            log.info("resolver ex", e);
        }

        return null; // null로 return을 하면 기존에 발생한 예외를 서블릿 밖으로 던진다.
    }
}
