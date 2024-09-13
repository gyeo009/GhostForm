package com.gdscGCC.ghostform.Service;

import com.gdscGCC.ghostform.Dto.Login.LoginDto;
import com.gdscGCC.ghostform.Dto.User.UserRequestDto;
import com.gdscGCC.ghostform.Dto.User.UserResponseDto;
import com.gdscGCC.ghostform.Entity.User;
import com.gdscGCC.ghostform.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** user의 id로 유저 정보를 찾는 서비스 */
    @Transactional
    public Optional<UserResponseDto> findById(String _id) {
        User tmpUser = userRepository.findById(_id).orElseThrow(() -> new UsernameNotFoundException("User Not Found."));
        return Optional.of(new UserResponseDto(tmpUser));
    }

    /** 회원가입 서비스 */
    @Transactional
    public UserResponseDto join(UserRequestDto userRequestDto) {
        User newUser = User.newUserBuilder(userRequestDto, passwordEncoder);
        return new UserResponseDto(userRepository.save(newUser));
    }

    @Transactional
    public boolean login(LoginDto loginDto) {
        User tmpUser = userRepository.findById(loginDto.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return tmpUser.getPassword().equals(loginDto.getPassword());
    }

    /** 유저 정보 업데이트 */
    @Transactional
    public void update(UserRequestDto userRequestDto) {
        User tmpUser = userRepository.findById(userRequestDto.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found."));
        tmpUser.update(userRequestDto.getName(), userRequestDto.getEmail());
    }

    /** 유저 삭제 */
    @Transactional
    public void delete(UserRequestDto userRequestDto) {
        User tmpUser = userRepository.findById(userRequestDto.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found."));
        userRepository.delete(tmpUser);
    }

    /** ID 중복 검사 */
    @Transactional
    public Boolean checkDuplicatedID(String userId) {
        Optional<User> checkUser = userRepository.findById(userId);
        return checkUser.isPresent();
    }
}
