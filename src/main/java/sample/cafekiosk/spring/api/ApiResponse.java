package sample.cafekiosk.spring.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of (HttpStatus httpStatus, String message, T date) {
        return new ApiResponse<>(httpStatus, message, date);
    }

    public static <T> ApiResponse<T> of (HttpStatus httpStatus, T date) {
        return new ApiResponse<>(httpStatus, httpStatus.name(), date);
    }

    public static <T> ApiResponse<T> ok (T date) {
        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), date);
    }
}
