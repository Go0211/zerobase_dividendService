package zerobase.dividend.service.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistUserException extends AbstractException{
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 아이디 ";
    }
}
