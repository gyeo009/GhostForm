package com.gdscGCC.ghostform.Dto.Variable;

import com.gdscGCC.ghostform.Entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class VariableRequestDto {
    private String key;
    private Object value;

}
