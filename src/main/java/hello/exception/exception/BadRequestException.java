package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * API 예외 처리 - 스프링이 제공하는 ExceptionResolver1 (ResponseStatusExceptionResolver)
 * 원래 Exception이 터지면 500 예외가 나와야 한다.
 * @ResponseStatus로 400 예외를 만들 수 있다.
 */
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "잘못된 요청 오류")
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") // reason 을 MessageSource 에서 찾는 기능도 제공한다.
public class BadRequestException extends RuntimeException {
}
