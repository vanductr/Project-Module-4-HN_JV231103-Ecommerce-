package rikkei.academy.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rikkei.academy.model.dto.response.DataError;
import rikkei.academy.model.dto.response.ResponseError;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class APIControllerAdvice {
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String,Object> error(AccessDeniedException e){
        Map<String,Object> map = new HashMap<>();
        map.put("error",new ResponseError(500,"FOR_BIDDEN",e));
        return  map;
    }

    // Bắt ngoại lệ và thông báo cho Lỗi: Không tìm thấy Sản phẩm theo Id
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DataError<String> handleErrNotFound(NoSuchElementException e) {
        return new DataError<>("Không tìm thấy tài nguyên phù hợp", HttpStatus.NOT_FOUND);
    }
}
