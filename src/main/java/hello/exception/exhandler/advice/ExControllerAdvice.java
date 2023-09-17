package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// API 예외 처리 - @ControllerAdvice
@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {
    // 여러 Controller에서 발생한 오류들을 ExControllerAdvice클래스에서 모아서 처리해준다.

    // 예외 처리하는 부분
    // getMember() IllegalArgumentException 예외 처리
    // @ExceptionHandler는 해당 컨트롤러에서 IllegalArgumentException 예외가 발생하면
    // illegalExHandler() 메서드가 잡는다. (다른 컨트롤러에는 @ExceptionHandler 적용이 안된다.)
    // ErrorResult가 json으로 반환이 된다.
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 예외 상태 코드를 바꾸고 싶다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    // getMember() UserException 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST); // ResponseEntity 생성자
    }

    // 그냥 Exception 터진다면
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
