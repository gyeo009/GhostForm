package com.gdscGCC.ghostform.Controller;

import com.gdscGCC.ghostform.Dto.Login.LoginDto;
import com.gdscGCC.ghostform.Dto.User.UserRequestDto;
import com.gdscGCC.ghostform.Dto.User.UserResponseDto;
import com.gdscGCC.ghostform.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User API")
public class UserAPIController {
    private final UserService userService;

    /** 회원가입 API */
    @PostMapping("/auth")
    public ResponseEntity<UserResponseDto> userAuth(@RequestBody UserRequestDto userRequestDto) throws Exception {
        UserResponseDto response = userService.join(userRequestDto);
        if (response == null) {
            System.out.println("Auth Error");
            log.info("Auth Error");
            throw new Exception("Error");
        } else {
            System.out.println("Auth Complete");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /** 유저 정보 API */
    @PostMapping("/update")
    public ResponseEntity<UserRequestDto> userUpdate(@RequestBody UserRequestDto userRequestDto) {
        userService.update(userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(userRequestDto);
    }

    /** 유저 삭제 API */
    @PostMapping("/remove")
    public ResponseEntity<UserRequestDto> userRemove(@RequestBody UserRequestDto userRequestDto) {
        userService.update(userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(userRequestDto);
    }

    /** 로그인 API */
    @PostMapping("/login")
    public void userLogin(@RequestBody LoginDto loginDto) {
        if (userService.login(loginDto)) System.out.println("Login Success");
        else System.out.println("Login Failed");
    }

}