package nx.ESE.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.Subject;
import nx.ESE.dtos.SubjectDto;
import nx.ESE.repositories.CourseRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;

@Controller
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

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

	// Exceptions*********************
	public boolean existsById(String id) {
		return subjectRepository.existsById(id);
	}

	// CRUD******************************
	public Optional<List<SubjectDto>> getFullSubjects() {
		List<SubjectDto> list = subjectRepository.findAll(new Sort(Sort.Direction.ASC, "name"))
				.stream()
				.map(s -> new SubjectDto(s))
				.parallel()
				.sorted((s1,s2) -> s1.getName().toString().compareTo(s2.getName().toString()))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		//list.forEach(s-> System.out.println(s.getName()));
		return Optional.of(list);

	}

	public Optional<SubjectDto> getSubjectById(String id) {
		Optional<Subject> subject = subjectRepository.findById(id);
		if (subject.isPresent())
			return Optional.of(new SubjectDto(subject.get()));
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

}
