package com.gdscGCC.ghostform.Dto.Ask;


import com.gdscGCC.ghostform.Entity.Ask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AskRequestDto {
    long id;
    List<Ask> askList;
}
