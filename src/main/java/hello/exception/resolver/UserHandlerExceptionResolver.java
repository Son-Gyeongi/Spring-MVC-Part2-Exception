package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// API 예외 처리 - HandlerExceptionResolver 활용
// UserExcetption을 서블릿 컨테이너까지 안가고 HandlerException에서 끝내보자
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    // 아래 errorResult를 json으로 바꾸기 위해서 ObjectMapper 필요
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                // 똑똑하게 처리하려면 HTTP 헤더에 따라서 2가지 처리해주어야 한다.
                // http헤더가 json인 경우와 아닌 경우(html로 요청 온 경우)
                String acceptHeader = request.getHeader("accept"); // 'accept' 헤더를 꺼낸다.
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // http 상태코드 400으로 바꿔주기, 응답이 나갈때 400으로 나간다.

                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass()); // ex 예외정보에 예외 클래스 정보를 넣어준다. 어떤 Exception이 터졌는지 넘겨준다.
                    errorResult.put("message", ex.getMessage());

                    // errorResult를 json으로 바꾸기 위해서 ObjectMapper 사용
                    String result = objectMapper.writeValueAsString(errorResult);// errorResult 객체를 json으로 바꾸고 그 다음 json을 문자로 바꿔준다.

                    // ModelAndView 반환을 위해서 세팅해준다.
                    response.setContentType("application/json"); // 응답에 contentType 알려준다.
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);

                    return new ModelAndView(); // 예외는 먹고 정상적으로 반환
                } else {
                    // TEXT/HTML
                    return new ModelAndView("error/500"); // templates/error/500.html 호출
                }
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
