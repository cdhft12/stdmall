package com.std.stdmall.domain.posts;

import com.std.stdmall.domain.Board;
import com.std.stdmall.domain.BoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardTest {

    @Autowired
    BoardRepository postsRepository;

    @AfterEach
    void afterEach() {
        postsRepository.deleteAll();
    }

    @Test
    public void test() {
        //given
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Board.builder()
                .title("제목")
                .content("내용")
                .author("테스터")
                .build());
        //when
        List<Board> postsList = postsRepository.findAll();
        //then
        Board board = postsList.get(0);
        assertThat(board.getTitle()).isEqualTo("제목");
        assertThat(board.getContent()).isEqualTo("내용");
        assertTrue(board.getCreatedDate().isAfter(now));
        assertTrue(board.getModifiedDate().isAfter(now));
    }
}
