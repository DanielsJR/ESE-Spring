package nx.ESE.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Quiz;
import nx.ESE.dtos.QuizDto;
import nx.ESE.repositories.QuizRepository;
import nx.ESE.repositories.UserRepository;

@Service
public class QuizService {
	
	@Autowired
	private UserRepository userRepository;


	@Autowired
	private QuizRepository quizRepository;
	
	
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

	public boolean existsById(String id) {
		return quizRepository.existsById(id);
	}

	public boolean isIdNull(@Valid QuizDto quizDto) {
		return quizDto.getId() == null;
	}

	
	// CRUD******************************
	public Optional<QuizDto> getQuizById(String id) {
		Optional<Quiz> quiz = quizRepository.findById(id);
		if (quiz.isPresent())
			return Optional.of(new QuizDto(quiz.get()));
		return Optional.empty();
	}

	public Optional<List<QuizDto>> getFullQuizes() {
		List<QuizDto> list = quizRepository.findAll(new Sort(Sort.Direction.ASC, "title"))
				.stream()
				.map(q -> new QuizDto(q))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

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


}
