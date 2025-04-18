package com.std.stdmall.web;

import com.std.stdmall.board.repository.BoardRepository;
import com.std.stdmall.board.dto.BoardSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WebRestController {

    private BoardRepository boardRepository;

    @PostMapping("/board")
    public void savePosts(@RequestBody BoardSaveRequestDto dto){
        boardRepository.save(dto.toEntity());
    }
}
