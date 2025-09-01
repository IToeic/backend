package kr.mojuk.itoeic.word.word;

import kr.mojuk.itoeic.test.testRepository.ProgressesRepository;
import kr.mojuk.itoeic.test.tsetEntity.Progresses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import kr.mojuk.itoeic.user.UsersRepository;
import kr.mojuk.itoeic.user.Users;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ProgressesRepository progressesRepository; // 의존성 주입 추가
    private final UsersRepository usersRepository;

    // 🔥 [로직 변경] 특정 단어팩과 사용자에 대해 오늘 학습할 단어 5개를 반환
    @Transactional
    public List<WordDTO.Response> getRandomWordsByPack(Integer wordpackId, String userId) {
        // 1. 오늘 날짜로 Progresses에서 단어 조회
        LocalDate today = LocalDate.now();
        List<Progresses> todayProgresses = progressesRepository.findByUser_UserIdAndLearnDateAndStatusNot(userId, today, Progresses.Status.COMPLETED);
        
        List<Word> wordsFromProgress = todayProgresses.stream()
                .map(Progresses::getWord)
                .collect(Collectors.toList());

        int progressWordCount = wordsFromProgress.size();

        // 2. 단어 개수에 따른 분기 처리
        // CASE 1: 오늘 학습할 단어가 5개 이상인 경우
        if (progressWordCount >= 5) {
            return wordsFromProgress.stream()
                    .limit(5)
                    .map(WordDTO.Response::fromEntity)
                    .collect(Collectors.toList());
        }

        // CASE 2: 오늘 학습할 단어가 하나도 없는 경우 (새로운 랜덤 단어 생성)
        if (progressWordCount == 0) {
            List<Word> randomWords = wordRepository.findRandom5ByWordPackId(wordpackId);
            
            // 🔥 랜덤 단어들을 progresses 테이블에 PENDING 상태로 저장
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));
            
            List<Progresses> newProgresses = new ArrayList<>();
            for (Word word : randomWords) {
                // 중복 저장을 방지하기 위해 이미 해당 단어에 대한 진행 기록이 있는지 확인
                boolean exists = !progressesRepository.findByUserIdAndWordIds(userId, List.of(word.getWordId())).isEmpty();
                if (!exists) {
                    Progresses progress = Progresses.builder()
                            .user(user)
                            .word(word)
                            .build(); // status는 엔티티에서 PENDING으로 기본 설정됨
                    
                    newProgresses.add(progress);
                }
            }

            if (!newProgresses.isEmpty()) {
                progressesRepository.saveAll(newProgresses);
            }
            
            return randomWords.stream()
                    .map(WordDTO.Response::fromEntity)
                    .collect(Collectors.toList());
        }

        // CASE 3: 오늘 학습할 단어가 1~4개인 경우 (부족한 만큼 랜덤 추가)
        int neededCount = 5 - progressWordCount;
        List<Integer> excludedWordIds = wordsFromProgress.stream()
                .map(Word::getWordId)
                .collect(Collectors.toList());
        
        List<Word> randomWords = wordRepository.findRandomWordsByPackIdExcludingIds(
                wordpackId, excludedWordIds, neededCount);

        // 🔥 추가된 랜덤 단어들을 progresses 테이블에 PENDING 상태로 저장
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));
        
        List<Progresses> newProgresses = new ArrayList<>();
        for (Word word : randomWords) {
            // 중복 저장을 방지하기 위해 이미 해당 단어에 대한 진행 기록이 있는지 확인
            boolean exists = !progressesRepository.findByUserIdAndWordIds(userId, List.of(word.getWordId())).isEmpty();
            if (!exists) {
                Progresses progress = Progresses.builder()
                        .user(user)
                        .word(word)
                        .build(); // status는 엔티티에서 PENDING으로 기본 설정됨
                
                newProgresses.add(progress);
            }
        }

        if (!newProgresses.isEmpty()) {
            progressesRepository.saveAll(newProgresses);
        }

        // 최종 단어 목록 조합
        List<Word> finalWords = new ArrayList<>(wordsFromProgress);
        finalWords.addAll(randomWords);

        return finalWords.stream()
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // [변경 없음] 특정 단어팩의 모든 단어를 반환합니다. (순서 섞음)
    public List<WordDTO.Response> getAllWordsByPack(Integer wordpackId) {
        List<Word> words = wordRepository.findByWordPack_WordpackId(wordpackId);
        Collections.shuffle(words); 
        return words.stream()
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // [변경 없음] 여러 단어팩 ID를 받아 해당하는 모든 단어를 반환합니다. (순서 섞음)
    public List<WordDTO.Response> getAllWordsByPackIds(List<Integer> wordpackIds) {
        List<Word> words = wordRepository.findAllByWordPack_WordpackIdIn(wordpackIds);
        Collections.shuffle(words);
        return words.stream()
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }
}