package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

// 서블릿 예외 처리 - 오류 화면 제공
// 오류 화면을 보여주기 위한 컨트롤러
@Slf4j
@Controller
public class ErrorPageController {

    // 서블릿 예외 처리 - 오류 페이지 작동 원리
    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    // GET, POST 등 한번에 처리 하기 위해서 @RequestMapping 썼다.
    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404"; // view 호출
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500"; // view 호출
    }

    // 서블릿 예외 처리 - 오류 페이지 작동 원리
    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatcherType= {}", request.getDispatcherType()); // 중요
    }

    // API 예외 처리 - 시작
    /**
     * 클라이언트는 정상 요청이든, 오류 요청이든 json이 반환되기를 기대한다.
     * 문제를 해결하려면 오류 페이지 컨트롤러도 json응답을 할 수 있도록 수정해야 한다.
     *
     * 클라이언트가 보내는 Accept(클라이언트가 받을 수 있는 데이터 타입) 타입에 따라서 "/error-page/500" 같은 url 이더라도
     * 클라이언트가 보내는 타입이 application/json인 경우 APPLICATION_JSON_VALUE가 우선순위 먼저이다.
     *
     * 서블릿 기본 오류 페이지 동작 메커니즘을 가지고 서블릿에 오류 페이지 등록(WebServerCustomizer)해서
     * api 오류를 어떻게 해결할 수 있는지에 대해서 알아보았다.
     */
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(
            HttpServletRequest request, HttpServletResponse response) {
        // json 반환 ResponseEntity

        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>(); // HashMap 순서 보장하지 않음
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage()); // 예외 메시지

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);// Http 오류 상태 코드

        // json이니깐 ResponseEntity는 http 응답 바디에 데이터를 바로 쏘는 거다.
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }
}