package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 서블릿 예외 처리 - 오류 화면 제공
// 오류 화면을 보여주기 위한 컨트롤러
@Slf4j
@Controller
public class ErrorPageController {

    // GET, POST 등 한번에 처리 하기 위해서 @RequestMapping 썼다.
    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        return "error-page/404"; // view 호출
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        return "error-page/500"; // view 호출
    }
}
