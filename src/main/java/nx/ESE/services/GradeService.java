package nx.ESE.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Evaluation;
import nx.ESE.documents.core.Grade;
import nx.ESE.documents.core.QuizStudent;
import nx.ESE.dtos.EvaluationDto;
import nx.ESE.dtos.GradeDto;
import nx.ESE.dtos.QuizStudentDto;
import nx.ESE.repositories.EvaluationRepository;
import nx.ESE.repositories.GradeRepository;
import nx.ESE.repositories.QuizStudentRepository;
import nx.ESE.repositories.UserRepository;

@Service
public class GradeService {

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private QuizStudentRepository quizStudentRepository;

	private Grade setGradeFromDto(Grade grade, @Valid GradeDto gradeDto) {
		grade.setGrade(gradeDto.getGrade());
		grade.setStudent(this.setStudent(gradeDto).get());
		grade.setEvaluation(this.setEvaluation(gradeDto).get());
		if (gradeDto.getQuizStudent() != null) {
			grade.setQuizStudent(this.setQuizStudent(gradeDto).get());
		}
		return grade;
	}

	private Optional<User> setStudent(GradeDto gradeDto) {
		Optional<User> student = userRepository.findById(gradeDto.getStudent().getId());
		if (student.isPresent())
			return student;
		return Optional.empty();
	}

	private Optional<Evaluation> setEvaluation(GradeDto gradeDto) {
		Optional<Evaluation> evaluation = evaluationRepository.findById(gradeDto.getEvaluation().getId());

		if (evaluation.isPresent())
			return evaluation;
		return Optional.empty();
	}

	private Optional<QuizStudent> setQuizStudent(GradeDto gradeDto) {
		Optional<QuizStudent> quizStudent = quizStudentRepository.findById(gradeDto.getQuizStudent().getId());

		if (quizStudent.isPresent())
			return quizStudent;
		return Optional.empty();
	}

	// CRUD******************************
	public GradeDto createGrade(@Valid GradeDto gradeDto) {
		Grade grade = new Grade();

		gradeRepository.insert(setGradeFromDto(grade, gradeDto));
		return new GradeDto(gradeRepository.findById(grade.getId()).get());
	}

	public Optional<GradeDto> modifyGrade(String id, @Valid GradeDto gradeDto) {
		Optional<Grade> grade = gradeRepository.findById(id);
		if (grade.isPresent()) {
			gradeRepository.save(setGradeFromDto(grade.get(), gradeDto));
			return Optional.of(new GradeDto(gradeRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<GradeDto> deleteGrade(String id) {
		Optional<Grade> grade = gradeRepository.findById(id);
		if (grade.isPresent()) {
			gradeRepository.deleteById(id);
			return Optional.of(new GradeDto(grade.get()));
		}
		return Optional.empty();
	}

	public Optional<List<GradeDto>> getFullGrades() {
		List<GradeDto> list = gradeRepository.findAll(new Sort(Sort.Direction.ASC, "title")).stream()
				.map(g -> new GradeDto(g)).collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		// list.forEach(g-> System.out.println(g.getStudent().getFirstName()));
		return Optional.of(list);
	}

	public Optional<List<GradeDto>> getGradesBySubject(String id) {
		// List<EvaluationDto> evaluationList =
		// this.evaluationService.getEvaluationsBySubject(id).get();
		List<EvaluationDto> evaluationList = this.evaluationRepository.findBySubject(id);

		List<GradeDto> list = evaluationList.stream().map(e -> gradeRepository.findByEvaluation(e.getId()))
				.flatMap(List::stream).collect(Collectors.toList());

		if (list.isEmpty())
			return Optional.empty();
		// list.forEach(g-> System.out.println(g.getStudent().getFirstName()));
		return Optional.of(list);
	}

	public Optional<GradeDto> getGradeById(String id) {
		Optional<Grade> grade = gradeRepository.findById(id);
		if (grade.isPresent())
			return Optional.of(new GradeDto(grade.get()));
		return Optional.empty();

	}

	// Exceptions*********************
	public boolean existsById(String id) {
		return gradeRepository.existsById(id);
	}

	public boolean isIdNull(@Valid GradeDto gradeDto) {
		return gradeDto.getId() == null;
	}

	public boolean isStudentNull(@Valid GradeDto gradeDto) {
		return gradeDto.getStudent() == null;
	}

	public boolean isEvaluationNull(@Valid GradeDto gradeDto) {
		return gradeDto.getEvaluation() == null;
	}

	public boolean isGradeRepeated(@Valid GradeDto gradeDto) {
		if (isStudentNull(gradeDto) || isEvaluationNull(gradeDto)) {
			return false;
		}

		GradeDto gradeDB = gradeRepository.findByStudentAndEvaluation(gradeDto.getStudent().getId(),
				gradeDto.getEvaluation().getId());
		return gradeDB != null && !gradeDB.getId().equals(gradeDto.getId());
	}

}
