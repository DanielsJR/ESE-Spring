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
import nx.ESE.documents.core.QuizStudent;
import nx.ESE.documents.core.Subject;
import nx.ESE.dtos.QuizDto;
import nx.ESE.dtos.QuizStudentDto;
import nx.ESE.repositories.QuizRepository;
import nx.ESE.repositories.QuizStudentRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;

@Service
public class QuizStudentService {
	
	@Autowired
	private UserRepository userRepository;


	@Autowired
	private QuizRepository quizRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private QuizStudentRepository quizStudentRepository;
	
	
	private QuizStudent setQuizStudentFromDto(QuizStudent quizStudent, @Valid QuizStudentDto quizStudentDto) {
		quizStudent.setDate(quizStudentDto.getDate());
		quizStudent.setStudent(this.setStudent(quizStudentDto).get());
	    quizStudent.setSubject(this.setSubject(quizStudentDto).get());
	    quizStudent.setQuiz(this.setQuiz(quizStudentDto).get());
        quizStudent.setCorrespondItemAnswers(quizStudentDto.getCorrespondItemAnswers());
        quizStudent.setIncompleteTextItemAnswers(quizStudentDto.getIncompleteTextItemAnswers());
        quizStudent.setTrueFalseItemAnswers(quizStudentDto.getTrueFalseItemAnswers());
        quizStudent.setMultipleSelectionIitemAnswers(quizStudentDto.getCorrespondItemAnswers());
        
		return quizStudent;
	}

	private Optional<Quiz> setQuiz(@Valid QuizStudentDto quizStudentDto) {
		Optional<Quiz> quiz = quizRepository.findById(quizStudentDto.getQuizDto().getId());
		if (quiz.isPresent())
			return quiz;
		return Optional.empty();
	}

	private Optional<Subject> setSubject(@Valid QuizStudentDto quizStudentDto) {
		Optional<Subject> subject = subjectRepository.findById(quizStudentDto.getSubjectDto().getId());
		if (subject.isPresent())
			return subject;
		return Optional.empty();
	}

	private Optional<User> setStudent(QuizStudentDto quizStudentDto) {
		Optional<User> student = userRepository.findById(quizStudentDto.getStudentDto().getId());
		if (student.isPresent())
			return student;
		return Optional.empty();
	}

	public boolean existsById(String id) {
		return quizStudentRepository.existsById(id);
	}

	public boolean isIdNull(@Valid QuizStudentDto quizStudentDto) {
		return quizStudentDto.getId() == null;
	}
	
	// CRUD******************************
	public Optional<QuizStudentDto> getQuizStudentById(String id) {
		Optional<QuizStudent> quizStudent = quizStudentRepository.findById(id);
		if (quizStudent.isPresent())
			return Optional.of(new QuizStudentDto(quizStudent.get()));
		return Optional.empty();
	}

	public Optional<List<QuizStudentDto>> getFullQuizesStudent() {
		List<QuizStudentDto> list = quizStudentRepository.findAll(new Sort(Sort.Direction.ASC, "grade"))
				.stream()
				.map(qs -> new QuizStudentDto(qs))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}


	public QuizStudentDto createQuizStudent(@Valid QuizStudentDto quizStudentDto) {
		QuizStudent quizStudent = new QuizStudent();
		quizStudentRepository.insert(setQuizStudentFromDto(quizStudent, quizStudentDto));
		return new QuizStudentDto(quizStudentRepository.findById(quizStudent.getId()).get());
	}

	public Optional<QuizStudentDto> modifyQuizStudent(String id, @Valid QuizStudentDto quizStudentDto) {
		Optional<QuizStudent> quizStudent = quizStudentRepository.findById(id);
		if (quizStudent.isPresent()) {
			quizStudentRepository.save(setQuizStudentFromDto(quizStudent.get(), quizStudentDto));
			return Optional.of(new QuizStudentDto(quizStudentRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<QuizStudentDto> deleteQuizStudent(String id) {
		Optional<QuizStudent> quizStudent = quizStudentRepository.findById(id);
		if (quizStudent.isPresent()) {
			quizStudentRepository.deleteById(id);
			return Optional.of(new QuizStudentDto(quizStudent.get()));
		}
		return Optional.empty();
	}

}
