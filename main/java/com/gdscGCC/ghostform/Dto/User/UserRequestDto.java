package com.gdscGCC.ghostform.Dto.User;

import com.gdscGCC.ghostform.Entity.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@ToString
@NoArgsConstructor
public class UserRequestDto {

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

}
