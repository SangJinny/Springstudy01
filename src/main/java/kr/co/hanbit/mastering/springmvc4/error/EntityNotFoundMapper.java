package kr.co.hanbit.mastering.springmvc4.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jeon on 2017-04-02.
 */
/* @ControllerAdvice는 빈에 의해 컨트롤러들을 대상으로 한 동작들을 추가할 수 있게한다.
 * 예외처리도 가능하고, @ModelAttribute를 이용한 모델 애트리뷰트나
 * @InitBinder를 이용한 정책검증도
 * 가능하다.*/
@ControllerAdvice
public class EntityNotFoundMapper {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity could not be found")
    public void handleNotFound() {
        //
    }
}
