package nx.ESE.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.Subject;
import nx.ESE.documents.core.SubjectName;
import nx.ESE.dtos.SubjectDto;
import nx.ESE.repositories.CourseRepository;
import nx.ESE.repositories.EvaluationRepository;
import nx.ESE.repositories.QuizStudentRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private QuizStudentRepository quizStudentRepository;

	@Autowired
	private EvaluationRepository evaluationRepository;

	private Subject setSubjectFromDto(Subject subject, SubjectDto subjectDto) {
		subject.setName(subjectDto.getName());
		subject.setTeacher(setTeacher(subjectDto).get());
		subject.setCourse(setCourse(subjectDto).get());
		return subject;
	}

	private Optional<User> setTeacher(SubjectDto subjectDto) {
		Optional<User> teacher = userRepository.findById(subjectDto.getTeacher().getId());
		if (teacher.isPresent())
			return teacher;
		return Optional.empty();
	}

	private Optional<Course> setCourse(SubjectDto subjectDto) {
		Optional<Course> course = courseRepository.findById(subjectDto.getCourse().getId());
		if (course.isPresent())
			return course;
		return Optional.empty();
	}

	// CRUD******************************
	public Optional<List<SubjectDto>> getFullSubjects() {
		List<SubjectDto> list = subjectRepository.findAll(new Sort(Sort.Direction.ASC, "name")).stream()
				.map(s -> new SubjectDto(s))
				.parallel()
				.sorted((s1, s2) -> s1.getName().toString().compareTo(s2.getName().toString()))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		// list.forEach(s-> System.out.println(s.getName()));
		return Optional.of(list);

	}
	
	public Optional<List<SubjectDto>> getSubjectsByTeacher(String id) {
		List<SubjectDto> list = subjectRepository.findByTeacher(id);
				//.stream()
				//.parallel()
				//.sorted((s1, s2) -> s1.getName().toString().compareTo(s2.getName().toString()))
				//.collect(Collectors.toList())
		if (list.isEmpty())
			return Optional.empty();
		// list.forEach(s-> System.out.println(s.getName()));
		return Optional.of(list);
	}
	
	public Optional<List<SubjectDto>> getSubjectsByCourse(String id) {
		List<SubjectDto> list = subjectRepository.findByCourse(id);
				//.stream()
				//.parallel()
				//.sorted((s1, s2) -> s1.getName().toString().compareTo(s2.getName().toString()))
				//.collect(Collectors.toList())
		if (list.isEmpty())
			return Optional.empty();
		// list.forEach(s-> System.out.println(s.getName()));
		return Optional.of(list);
	}


	public Optional<SubjectDto> getSubjectById(String id) {
		Optional<Subject> subject = subjectRepository.findById(id);
		if (subject.isPresent())
			return Optional.of(new SubjectDto(subject.get()));
		return Optional.empty();
	}

	public Optional<SubjectDto> getSubjectByNameAndCourse(SubjectName name, String courseId) {
		SubjectDto subjectDto = subjectRepository.findByNameAndCourse(name, courseId);
		if (subjectDto != null)
			return Optional.of(subjectDto);
		return Optional.empty();
	}

	public SubjectDto createSubject(@Valid SubjectDto subjectDto) {
		Subject subject = new Subject();
		subjectRepository.insert(setSubjectFromDto(subject, subjectDto));
		return new SubjectDto(subjectRepository.findById(subject.getId()).get());
	}

	public Optional<SubjectDto> modifySubject(String id, @Valid SubjectDto subjectDto) {
		Optional<Subject> subject = subjectRepository.findById(id);
		if (subject.isPresent()) {
			subjectRepository.save(setSubjectFromDto(subject.get(), subjectDto));
			return Optional.of(new SubjectDto(subjectRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<SubjectDto> deleteSubject(String id) {
		Optional<Subject> subject = subjectRepository.findById(id);
		if (subject.isPresent()) {
			subjectRepository.deleteById(id);
			return Optional.of(new SubjectDto(subject.get()));
		}
		return Optional.empty();
	}

	// Exceptions*********************
	public boolean existsById(String id) {
		return subjectRepository.existsById(id);
	}

	public boolean isIdNull(@Valid SubjectDto subjectDto) {
		return subjectDto.getId() == null;
	}

	public boolean isNameNull(@Valid SubjectDto subjectDto) {
		return subjectDto.getName() == null;
	}

	public boolean isTeacherNull(@Valid SubjectDto subjectDto) {
		return subjectDto.getTeacher() == null;
	}

	public boolean isCourseNull(@Valid SubjectDto subjectDto) {
		return subjectDto.getCourse() == null;
	}

	public boolean isSubjectRepeated(@Valid SubjectDto subjectDto) {
		if (this.isNameNull(subjectDto) || this.isCourseNull(subjectDto)) {
			return false;
		}
		SubjectDto subjectDB = this.subjectRepository.findByNameAndCourse(subjectDto.getName(),
				subjectDto.getCourse().getId());
		return subjectDB != null && !subjectDB.getId().equals(subjectDto.getId());
	}

	public boolean isSubjectInEvaluation(String subjectId) {
		return evaluationRepository.findFirstBySubject(subjectId) != null;
	}



}
