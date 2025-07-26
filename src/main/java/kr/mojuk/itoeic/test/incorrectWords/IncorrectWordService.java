package kr.mojuk.itoeic.test.incorrectWords;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.mojuk.itoeic.test.testRepository.IncorrectWordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncorrectWordService {
	
	private final IncorrectWordRepository incorrectWordRepository;
	
	public List<IncorrectWordResponseDTO> getIncorrectWordsByUserId(String userId) {
        return incorrectWordRepository.findByUser_UserId(userId)
                .stream()
                .map(IncorrectWordResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteIncorrectWord(Integer incorrectWordId) {
        incorrectWordRepository.deleteById(incorrectWordId);
    }
}
