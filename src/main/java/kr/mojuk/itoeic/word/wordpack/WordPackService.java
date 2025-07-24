package kr.mojuk.itoeic.word.wordpack;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordPackService {

    private final WordPackRepository wordPackRepository;

    public List<WordPackDTO.Response> getAllWordPacks() {
        return wordPackRepository.findAll()
                .stream()
                .map(WordPackDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
