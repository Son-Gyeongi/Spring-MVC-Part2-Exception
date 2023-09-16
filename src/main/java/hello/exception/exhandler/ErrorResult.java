package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

// API 예외 처리 - @ExceptionHandler
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code; // 오류가 발생했을 때 코드
    private String message; // 오류가 발생했을 때 메시지
}
