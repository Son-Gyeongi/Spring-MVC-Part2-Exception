package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 서블릿 예외 처리 - 시작
@Slf4j
@Controller
public class ServletExController {

    // 서블릿 컨테이너(WAS)에게 문제가 있다고 알려주는 방법 2가지
    // 1. Exception(예외)
    @GetMapping("/error-ex")
    public void errorEx() {
        // 그냥 예외 던지자
        throw new RuntimeException("예외 발생!");
        // Exception 터진거는 무조건 500으로 나간다.
    }
    // 2. response.sendError(HTTP 상태 코드, 오류 메시지) 호출
    // sendError는 상태코드 지정할 수 있다. 오류메시지는 default로 숨겨져서 안 보인다.
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }
    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
