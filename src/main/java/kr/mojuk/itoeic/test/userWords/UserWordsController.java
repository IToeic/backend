package kr.mojuk.itoeic.test.userWords;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.mojuk.itoeic.test.userWords.DTO.*;
import lombok.*;

@RestController
@RequestMapping("/api/user-words")
@RequiredArgsConstructor
public class UserWordsController {

    private final UserWordsService userWordService;


    @GetMapping("/{userId}")
    public ResponseEntity<List<UserWordResponseDto>> getUserWords(@PathVariable("userId") String userId) {
        List<UserWordResponseDto> words = userWordService.getUserWords(userId);
        return ResponseEntity.ok(words);
    }


    @PostMapping("/{userId}")
    public ResponseEntity<Void> addWord(@RequestBody UserWordRequestDto requestDto) {
        userWordService.addUserWord(requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteWord(@RequestBody UserWordRequestDto requestDto) {
        userWordService.deleteUserWord(requestDto);
        return ResponseEntity.ok().build();
    }
}
