package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormLogin;
import rikkei.academy.model.dto.request.FormRegister;
import rikkei.academy.model.dto.response.DataError;
import rikkei.academy.model.dto.response.JWTResponse;
import rikkei.academy.service.user.IUserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<JWTResponse> doLogin(@RequestBody FormLogin formLogin) throws DataExistException {
        return new ResponseEntity<>(userService.login(formLogin), HttpStatus.OK);
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> doRegister(@RequestBody FormRegister formRegister) {
        boolean check = userService.register(formRegister);
        if (check) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Đã tạo tài khoản thành công.");
            return new ResponseEntity<>(map, HttpStatus.CREATED);
        } else {
            throw new RuntimeException("Có lỗi gì đó xảy ra");
        }
    }
}
