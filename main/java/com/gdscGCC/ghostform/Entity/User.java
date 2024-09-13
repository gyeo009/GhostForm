package com.gdscGCC.ghostform.Entity;

import com.gdscGCC.ghostform.Dto.User.UserRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /** 사용자 번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ind_id;

    /** 사용자 이름 */
    private String name;
    /** 사용자 ID */
    private String id;
    /** 사용자 비밀번호 */
    private String password;
    /** 사용자 Email */
    private String email;

    public void update(String newName, String newEmail) {
        this.name = newName;
        this.email = newEmail;
    }

    public static User newUserBuilder(UserRequestDto userRequestDto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(userRequestDto.getId())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }
}
