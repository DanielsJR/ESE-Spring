package nx.ESE.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Grade;
import nx.ESE.documents.core.Subject;
import nx.ESE.dtos.GradeDto;
import nx.ESE.repositories.GradeRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;

@Service
public class GradeService {

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectRepository subjectRepository;


	private Grade setGradeFromDto(Grade grade, @Valid GradeDto gradeDto) {
		grade.setTitle(gradeDto.getTitle());
		grade.setType(gradeDto.getType());
		grade.setGrade(gradeDto.getGrade());
		grade.setDate(gradeDto.getDate());
		grade.setStudent(this.setStudent(gradeDto).get());
		grade.setSubject(this.setSubject(gradeDto).get());

		return grade;
	}

	private Optional<User> setStudent(GradeDto gradeDto) {
		Optional<User> student = userRepository.findById(gradeDto.getStudent().getId());
		if (student.isPresent())
			return student;
		return Optional.empty();
	}

	private Optional<Subject> setSubject(GradeDto gradeDto) {
		Optional<Subject> subject = subjectRepository.findById(gradeDto.getSubject().getId());
		if (subject.isPresent())
			return subject;
		return Optional.empty();
	}
	
	// Exceptions*********************
	public boolean existsById(String id) {
		return gradeRepository.existsById(id);
	}

	// CRUD******************************
	public Optional<List<GradeDto>> getFullGrades() {
		List<GradeDto> list = gradeRepository.findAll(new Sort(Sort.Direction.ASC, "title"))
				.stream()
				.map(g -> new GradeDto(g))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		//list.forEach(g-> System.out.println(g.getStudent().getFirstName()));
		return Optional.of(list);
	}

	public Optional<GradeDto> getGradeById(String id) {
		Optional<Grade> grade = gradeRepository.findById(id);
		if (grade.isPresent())
			return Optional.of(new GradeDto(grade.get()));
		return Optional.empty();

	}

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

}
