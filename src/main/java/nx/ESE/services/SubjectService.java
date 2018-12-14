package nx.ESE.services;

import java.util.ArrayList;
import java.util.List;

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
	private UserService userService;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseService courseService;

	public void setSubjectFromDto(Subject subject, SubjectDto subjectDto) {
		subject.setName(subjectDto.getName());
		subject.setTeacher(setTeacher(subjectDto));
		subject.setCourse(setCourse(subjectDto));
	}

	private User setTeacher(SubjectDto subjectDto) {
		User teacher = userRepository.findById(subjectDto.getTeacher().getId()).get();
		if (teacher != null)
			userService.setUserFromDto(teacher, subjectDto.getTeacher());
		return teacher;

	}

	private Course setCourse(SubjectDto subjectDto) {
		Course course = courseRepository.findById(subjectDto.getCourse().getId()).get();
		if (course != null)
			courseService.setCourseFromDto(course, subjectDto.getCourse());
		return course;

	}

	public boolean existsById(String id) {
		return subjectRepository.existsById(id);
	}

	// CRUD******************************
	public List<SubjectDto> getFullSubjects() {
		List<SubjectDto> listSubjects = new ArrayList<>();

		for (Subject subject : subjectRepository.findAll(new Sort(Sort.Direction.ASC, "name"))) {
			listSubjects.add(new SubjectDto(subject));
		}
		return listSubjects;
	}

	public SubjectDto getSubjectById(String id) {
		Subject subject = subjectRepository.findById(id).get();
		return new SubjectDto(subject);
	}

	public SubjectDto createSubject(@Valid SubjectDto subjectDto) {
		Subject subject = new Subject();
		setSubjectFromDto(subject, subjectDto);
		subjectRepository.insert(subject);
		return new SubjectDto(subjectRepository.findById(subject.getId()).get());
	}

	public SubjectDto modifySubject(String id, @Valid SubjectDto subjectDto) {
		Subject subject = subjectRepository.findById(id).get();
		setSubjectFromDto(subject, subjectDto);
		subjectRepository.save(subject);
		return new SubjectDto(subjectRepository.findById(subject.getId()).get());
	}

	public boolean deleteSubject(String id) {
		Subject subject = subjectRepository.findById(id).get();
		subjectRepository.delete(subject);
		return true;
	}

}
