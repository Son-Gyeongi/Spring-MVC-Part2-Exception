package hello.exception.api;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// api 호출 예제
@Slf4j
@RestController
public class ApiExceptionController {

    // API 예외 처리 - 시작
    // MemberDto 반환하는 API 만들기
    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        // IllegalArgumentException을 400에러로 처리, API 예외 처리 - HandlerExceptionResolver 시작
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        // API 예외 처리 - HandlerExceptionResolver 활용
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
