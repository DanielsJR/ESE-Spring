package nx.ese.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ese.documents.core.QuizStudent;
import nx.ese.dtos.QuizStudentDto;
import nx.ese.repositories.GradeRepository;
import nx.ese.repositories.QuizStudentRepository;
import nx.ese.repositories.SubjectRepository;
import nx.ese.repositories.UserRepository;

@Service
public class QuizStudentService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private QuizStudentRepository quizStudentRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	
	public QuizStudent setQuizStudentFromDto(QuizStudent quizStudent, @Valid QuizStudentDto quizStudentDto) {
        quizStudent.setCorrespondItems(quizStudentDto.getCorrespondItems());
        quizStudent.setIncompleteTextItems(quizStudentDto.getIncompleteTextItems());
        quizStudent.setTrueFalseItems(quizStudentDto.getTrueFalseItems());
        quizStudent.setMultipleSelectionItems(quizStudentDto.getMultipleSelectionItems());
        
		return quizStudent;
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


	public boolean isQuizStudentRepeated(@Valid QuizStudentDto quizStudentDto) {
		QuizStudentDto quizStudentDB = this.quizStudentRepository
				.findByCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
						quizStudentDto.getCorrespondItems(), quizStudentDto.getTrueFalseItems(),
						quizStudentDto.getMultipleSelectionItems(), quizStudentDto.getIncompleteTextItems());
		
		return quizStudentDB != null && !quizStudentDB.getId().equals(quizStudentDto.getId());
	}


	public boolean isQuizStudentInGrade(String id) {
		return gradeRepository.findFirstByQuizStudent(id) !=null;
	}

}
