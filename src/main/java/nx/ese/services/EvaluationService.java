package nx.ese.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ese.documents.core.Evaluation;
import nx.ese.documents.core.Quiz;
import nx.ese.documents.core.Subject;
import nx.ese.dtos.EvaluationDto;
import nx.ese.repositories.EvaluationRepository;
import nx.ese.repositories.QuizRepository;
import nx.ese.repositories.SubjectRepository;

@Service
public class EvaluationService {

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private QuizRepository quizRepository;

	// Exceptions*********************
	public boolean isIdNull(@Valid EvaluationDto evaluationDto) {
		return evaluationDto.getId() == null;
	}

	public boolean isEvaluationRepeated(@Valid EvaluationDto evaluationDto) {
		if (isTitleNull(evaluationDto) || isTypeNull(evaluationDto) || isSubjectNull(evaluationDto)
				|| isDateNull(evaluationDto)) {
			return false;
		}

		EvaluationDto evaluationDB = evaluationRepository.findByTitleAndTypeAndSubjectAndDate(evaluationDto.getTitle(),
				evaluationDto.getType(), evaluationDto.getSubject().getId(), evaluationDto.getDate());
		return evaluationDB != null && !evaluationDB.getId().equals(evaluationDto.getId());
	}

	private boolean isSubjectNull(@Valid EvaluationDto evaluationDto) {
		return evaluationDto.getSubject() == null;
	}

	private boolean isTypeNull(@Valid EvaluationDto evaluationDto) {
		return evaluationDto.getType() == null;
	}

	private boolean isTitleNull(@Valid EvaluationDto evaluationDto) {
		return evaluationDto.getTitle() == null;
	}

	public boolean isDateNull(@Valid EvaluationDto evaluationDto) {
		return evaluationDto.getDate() == null;
	}

	private Evaluation setEvaluationFromDto(Evaluation evaluation, @Valid EvaluationDto evaluationDto) {
		evaluation.setType(evaluationDto.getType());
		evaluation.setTitle(evaluationDto.getTitle());
		evaluation.setSubject(this.setSubject(evaluationDto).get());
		if (evaluationDto.getQuiz() != null) evaluation.setQuiz(this.setQuiz(evaluationDto).get());
		evaluation.setDate(evaluationDto.getDate());
		evaluation.setIsOpen(evaluationDto.getIsOpen());
		return evaluation;
	}

	private Optional<Quiz> setQuiz(EvaluationDto evaluationDto) {
		Optional<Quiz> quiz = quizRepository.findById(evaluationDto.getQuiz().getId());
		if (quiz.isPresent())
			return quiz;
		return Optional.empty();
	}

	private Optional<Subject> setSubject(EvaluationDto evaluationDto) {
		Optional<Subject> subject = subjectRepository.findById(evaluationDto.getSubject().getId());
		if (subject.isPresent())
			return subject;
		return Optional.empty();
	}

	// CRUD******************************
	public EvaluationDto createEvaluation(@Valid EvaluationDto evaluationDto) {
		Evaluation evaluation = setEvaluationFromDto(new Evaluation(), evaluationDto);
		evaluation.setIsOpen(true);
		evaluationRepository.insert(evaluation);
		return new EvaluationDto(evaluationRepository.findById(evaluation.getId()).get());
	}

	public Optional<EvaluationDto> modifyEvaluation(String id, @Valid EvaluationDto evaluationDto) {
		Optional<Evaluation> evaluation = evaluationRepository.findById(id);
		if (evaluation.isPresent()) {
			evaluationRepository.save(setEvaluationFromDto(evaluation.get(), evaluationDto));
			return Optional.of(new EvaluationDto(evaluationRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<EvaluationDto> deleteEvaluation(String id) {
		Optional<Evaluation> evaluation = evaluationRepository.findById(id);
		if (evaluation.isPresent()) {
			evaluationRepository.deleteById(id);
			return Optional.of(new EvaluationDto(evaluation.get()));
		}
		return Optional.empty();
	}

	public Optional<List<EvaluationDto>> getFullEvaluations() {
		List<EvaluationDto> list = evaluationRepository.findAll(new Sort(Sort.Direction.ASC, "title"))
				.stream()
				.map(e -> new EvaluationDto(e))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

	public Optional<EvaluationDto> getEvaluationById(String id) {
		Optional<Evaluation> evaluation = evaluationRepository.findById(id);
		if (evaluation.isPresent())
			return Optional.of(new EvaluationDto(evaluation.get()));
		return Optional.empty();
	}

	public Optional<List<EvaluationDto>> getEvaluationsBySubject(String subjectId) {
		List<EvaluationDto> list = evaluationRepository.findBySubject(subjectId);
				//.stream()
				//.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

}
