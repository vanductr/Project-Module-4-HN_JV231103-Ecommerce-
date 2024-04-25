package rikkei.academy.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResponseDtoSuccess<T> {
    private T duLieu;

    private HttpStatus trangThaiHTTP;
}
