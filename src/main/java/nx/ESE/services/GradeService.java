package nx.ESE.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Grade;
import nx.ESE.documents.core.Subject;
import nx.ESE.dtos.GradeDto;
import nx.ESE.dtos.SubjectDto;
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
	private UserService userService;
	
	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private SubjectService subjectService;

	public boolean existsById(String id) {
		return gradeRepository.existsById(id);
		
	}
	
	private User setStudent(GradeDto gradeDto) {
		User student = userRepository.findById(gradeDto.getStudent().getId()).get();
		if (student != null)
			userService.setUserFromDto(student, gradeDto.getStudent());
		return student;

	}
	
	
	private Subject setSubject(GradeDto gradeDto) {
		Subject subject = subjectRepository.findById(gradeDto.getSubject().getId()).get();
		if (subject != null)
			subjectService.setSubjectFromDto(subject, gradeDto.getSubject());
		return subject;

	}

	private void setGradeFromDto(Grade grade, @Valid GradeDto gradeDto) {
		grade.setTitle(gradeDto.getTitle());
		grade.setType(gradeDto.getType());
		grade.setGrade(gradeDto.getGrade());
		grade.setDate(gradeDto.getDate());
		grade.setStudent(this.setStudent(gradeDto));
		grade.setSubject(this.setSubject(gradeDto));

	}

	// CRUD******************************
	public List<GradeDto> getFullGrades() {
		List<GradeDto> listGrades = new ArrayList<>();

		for (Grade grade : gradeRepository.findAll(new Sort(Sort.Direction.ASC, "title"))) {
			listGrades.add(new GradeDto(grade));
		}
		return listGrades;

	}

	public GradeDto getGradeById(String id) {
		Grade grade = gradeRepository.findById(id).get();
		return new GradeDto(grade);

	}

	public GradeDto createGrade(@Valid GradeDto gradeDto) {
		Grade grade = new Grade();
		setGradeFromDto(grade, gradeDto);
		gradeRepository.insert(grade);
		return new GradeDto(gradeRepository.findById(grade.getId()).get());
	}

	public GradeDto modifyGrade(String id, @Valid GradeDto gradeDto) {
		Grade grade = gradeRepository.findById(id).get();
		setGradeFromDto(grade, gradeDto);
		gradeRepository.save(grade);
		return new GradeDto(gradeRepository.findById(grade.getId()).get());
	}

	public boolean deleteGrade(String id) {
		Grade grade = gradeRepository.findById(id).get();
		gradeRepository.delete(grade);
		return true;
	}

}
