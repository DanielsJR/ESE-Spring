package nx.ese.services;

import java.util.List;
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
		quiz.setAuthor(this.setAuthor(quizDto).get());
		quiz.setSubjectName(quizDto.getSubjectName());
		quiz.setQuizLevel(quizDto.getQuizLevel());
		quiz.setCorrespondItems(quizDto.getCorrespondItems());
		quiz.setIncompleteTextItems(quizDto.getIncompleteTextItems());
		quiz.setTrueFalseItems(quizDto.getTrueFalseItems());
		quiz.setMultipleSelectionItems(quizDto.getMultipleSelectionItems());
		return quiz;
	}

	private Optional<User> setAuthor(QuizDto quizDto) {
		Optional<User> author = userRepository.findById(quizDto.getAuthor().getId());
		if (author.isPresent())
			return author;
		return Optional.empty();
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
		return evaluationRepository.findFirstByQuiz(id) != null;
	}

	// CRUD******************************

	public QuizDto createQuiz(@Valid QuizDto quizDto) {
		Quiz quiz = new Quiz();
		quizRepository.insert(setQuizFromDto(quiz, quizDto));
		return new QuizDto(quizRepository.findById(quiz.getId()).get());
	}

	public Optional<QuizDto> modifyQuiz(String id, @Valid QuizDto quizDto) {
		Optional<Quiz> quiz = quizRepository.findById(id);
		if (quiz.isPresent()) {
			quizRepository.save(setQuizFromDto(quiz.get(), quizDto));
			return Optional.of(new QuizDto(quizRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<QuizDto> deleteQuiz(String id) {
		Optional<Quiz> quiz = quizRepository.findById(id);
		if (quiz.isPresent()) {
			quizRepository.deleteById(id);
			return Optional.of(new QuizDto(quiz.get()));
		}
		return Optional.empty();
	}
	
	

	public Optional<QuizDto> getQuizById(String id) {
		Optional<Quiz> quiz = quizRepository.findById(id);
		if (quiz.isPresent())
			return Optional.of(new QuizDto(quiz.get()));
		return Optional.empty();
	}

	public Optional<List<QuizDto>> getFullQuizes() {
		List<QuizDto> list = quizRepository.findAll(new Sort(Sort.Direction.ASC, "title")).stream()
				.map(q -> new QuizDto(q)).collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

	public Optional<List<QuizDto>> getFullQuizesByAuthor(String id) {
		List<QuizDto> list = quizRepository.findByAuthor(id);
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

}
