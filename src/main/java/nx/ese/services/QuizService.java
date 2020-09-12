package nx.ese.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ese.documents.User;
import nx.ese.documents.core.Quiz;
import nx.ese.dtos.QuizDto;
import nx.ese.repositories.EvaluationRepository;
import nx.ese.repositories.QuizRepository;
import nx.ese.repositories.UserRepository;

@Service
public class QuizService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    private Quiz setQuizFromDto(Quiz quiz, @Valid QuizDto quizDto) {
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setAuthor(this.setAuthor(quizDto));
        quiz.setSubjectName(quizDto.getSubjectName());
        quiz.setQuizLevel(quizDto.getQuizLevel());
        quiz.setShared(quizDto.getShared());

        quiz.setCorrespondItems(quizDto.getCorrespondItems());
        quiz.setIncompleteTextItems(quizDto.getIncompleteTextItems());
        quiz.setTrueFalseItems(quizDto.getTrueFalseItems());
        quiz.setMultipleSelectionItems(quizDto.getMultipleSelectionItems());
        return quiz;
    }

    private User setAuthor(QuizDto quizDto) {
        return userRepository.findById(quizDto.getAuthor().getId())
                .orElseThrow(() -> new NoSuchElementException("Author"));
    }

    // Exceptions*********************
    public boolean existsById(String id) {
        return quizRepository.existsById(id);
    }

    public boolean isIdNull(@Valid QuizDto quizDto) {
        return quizDto.getId() == null;
    }

    public boolean isTitleNull(@Valid QuizDto quizDto) {
        return quizDto.getTitle() == null;
    }

    public boolean isDescriptionNull(@Valid QuizDto quizDto) {
        return quizDto.getDescription() == null;
    }

    public boolean isSubjectNameNull(@Valid QuizDto quizDto) {
        return quizDto.getSubjectName() == null;
    }

    public boolean isQuizLevelNull(@Valid QuizDto quizDto) {
        return quizDto.getQuizLevel() == null;
    }

    public boolean isQuizRepeated(@Valid QuizDto quizDto) {
        if (this.isTitleNull(quizDto) || this.isDescriptionNull(quizDto) || this.isSubjectNameNull(quizDto)
                || this.isQuizLevelNull(quizDto)) {
            return false;
        }
        QuizDto quizDB = this.quizRepository
                .findByTitleAndAuthorAndSubjectNameAndQuizLevelAndCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
                        quizDto.getTitle(), quizDto.getAuthor().getId(), quizDto.getSubjectName().toString(),
                        quizDto.getQuizLevel().toString(), quizDto.getCorrespondItems(), quizDto.getTrueFalseItems(),
                        quizDto.getMultipleSelectionItems(), quizDto.getIncompleteTextItems());

        return quizDB != null && !quizDB.getId().equals(quizDto.getId());
    }

    public boolean isQuizInEvaluation(String id) {
        return evaluationRepository.findFirstByQuiz(id).isPresent();
    }

    public boolean isTeacherInQuiz(QuizDto quizDto, String username) {
        return quizDto.getAuthor().getUsername().equals(username);
    }

    // CRUD******************************
    public QuizDto createQuiz(@Valid QuizDto quizDto) {
        Quiz quiz = new Quiz();
        return new QuizDto(quizRepository.insert(setQuizFromDto(quiz, quizDto)));
    }

    public Optional<QuizDto> modifyQuiz(String id, @Valid QuizDto quizDto) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        return quiz.map(value -> new QuizDto(quizRepository.save(setQuizFromDto(value, quizDto))));
    }

    public Optional<QuizDto> deleteQuiz(String id, String teacherUsername) {
        Optional<Quiz> quiz = quizRepository.findById(id)
                .filter(quiz1 -> quiz1.getAuthor().getUsername().equals(teacherUsername));

        if (quiz.isPresent()) {
            quizRepository.deleteById(id);
            return quiz.map(QuizDto::new);
        }
        return Optional.empty();
    }


    public Optional<QuizDto> getQuizById(String id) {
        return quizRepository.findById(id)
                .map(QuizDto::new);
    }

    public Optional<QuizDto> getTeacherQuizById(String quizId, String teacherUsername) {
        return quizRepository.findById(quizId)
                .filter(quiz1 -> quiz1.getAuthor().getUsername().equals(teacherUsername))
                .map(QuizDto::new);
    }

    public Optional<List<QuizDto>> getQuizes() {
        return Optional.of(quizRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .stream()
                .map(QuizDto::new)
                .collect(Collectors.toList())
        );
    }

    public Optional<List<QuizDto>> getTeacherQuizes(String teacherUsername) {
        List<QuizDto> list = quizRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .stream()
                .filter(quiz1 -> quiz1.getAuthor().getUsername().equals(teacherUsername))
                .map(QuizDto::new)
                .collect(Collectors.toList());

        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<List<QuizDto>> getQuizesByAuthor(String id) {
        return Optional.of(quizRepository.findByAuthor(id));
    }

    public Optional<List<QuizDto>> getTeacherQuizesByAuthor(String id, String teacherUsername) {
        return Optional.of(quizRepository.findByAuthor(id)
                .stream()
                .filter(quiz1 -> (quiz1.getAuthor().getUsername().equals(teacherUsername)) || quiz1.getShared())
                .collect(Collectors.toList()));
    }
}
