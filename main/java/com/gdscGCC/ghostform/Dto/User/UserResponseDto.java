package com.gdscGCC.ghostform.Dto.User;

import com.gdscGCC.ghostform.Entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    /** 사용자 번호 */
    private final Long ind_id;

    /** 사용자 이름 */
    private final String name;
    /** 사용자 ID */
    private final String id;
    /** 사용자 비밀번호 */
    private final String password;
    /** 사용자 Email */
    private final String email;

    public UserResponseDto(User user) {
        this.ind_id = user.getInd_id();
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }
}
