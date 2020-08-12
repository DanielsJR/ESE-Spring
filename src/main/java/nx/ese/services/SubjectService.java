package nx.ese.services;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.querydsl.core.types.Predicate;
import nx.ese.documents.core.QSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ese.documents.User;
import nx.ese.documents.core.Course;
import nx.ese.documents.core.Subject;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.SubjectDto;
import nx.ese.repositories.CourseRepository;
import nx.ese.repositories.EvaluationRepository;
import nx.ese.repositories.QuizStudentRepository;
import nx.ese.repositories.SubjectRepository;
import nx.ese.repositories.UserRepository;

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
        subject.setTeacher(setTeacher(subjectDto));
        subject.setCourse(setCourse(subjectDto));
        return subject;
    }

    private User setTeacher(SubjectDto subjectDto) {
        return userRepository.findById(subjectDto.getTeacher().getId())
                .orElseThrow(NoSuchElementException::new);
    }

    private Course setCourse(SubjectDto subjectDto) {
        return courseRepository.findById(subjectDto.getCourse().getId())
                .orElseThrow(NoSuchElementException::new);
    }


    // CRUD******************************
    public SubjectDto createSubject(@Valid SubjectDto subjectDto) {
        Subject subject = new Subject();
        Subject ss = subjectRepository.insert(setSubjectFromDto(subject, subjectDto));
        return new SubjectDto(ss);
    }

    public Optional<SubjectDto> modifySubject(String id, @Valid SubjectDto subjectDto) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            return subject.map(s -> new SubjectDto(subjectRepository.save(setSubjectFromDto(s, subjectDto))));
        }
        return Optional.empty();
    }

    public Optional<SubjectDto> deleteSubject(String id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            subjectRepository.deleteById(id);
            return subject.map(SubjectDto::new);
        }
        return Optional.empty();
    }

    public Optional<List<SubjectDto>> getSubjectsByYear(String year) {

        List<SubjectDto> list = subjectRepository.findAll()
                .stream()
                .filter(s -> s.getCourse().getYear().equals(year))
                .sorted(Comparator.comparing(s -> s.getName().toString()))
                .map(SubjectDto::new)
                .collect(Collectors.toList());
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<SubjectDto> getSubjectById(String id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        return subject.map(SubjectDto::new);
    }

    public Optional<SubjectDto> getSubjectByNameAndCourse(SubjectName name, String courseId) {
        return subjectRepository.findByNameAndCourse(name, courseId);
    }

    public Optional<List<SubjectDto>> getSubjectsByTeacherAndYear(String username, String year) {
        Optional<User> u = userRepository.findByUsernameOptional(username);
        if (u.isPresent()) {
            List<SubjectDto> list = subjectRepository.findByTeacher(u.get().getId())
                    .stream()
                    .filter(s -> s.getCourse().getYear().equals(year))
                    .sorted(Comparator.comparing(s -> s.getName().toString()))
                    .collect(Collectors.toList());

            if (!list.isEmpty())
                return Optional.of(list);
        }
        return Optional.empty();
    }

    public Optional<List<SubjectDto>> getStudentSubjectsByCourse(String courseId, String username) {
        Optional<User> u = userRepository.findByUsernameOptional(username);
        Optional<Course> c = courseRepository.findById(courseId);
        if (u.isPresent() && c.isPresent() && this.isStudentInCourse(c.get().getId(), u.get().getId())) {
            List<SubjectDto> list = subjectRepository.findByCourse(courseId)
                    .stream()
                    .sorted(Comparator.comparing(s -> s.getName().toString()))
                    .collect(Collectors.toList());

            if (!list.isEmpty())
                return Optional.of(list);
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
        if (this.isNameNull(subjectDto) || this.isCourseNull(subjectDto))
            return false;

        Optional<SubjectDto> subjectDB = this.subjectRepository.findByNameAndCourse(subjectDto.getName(), subjectDto.getCourse().getId());
        return subjectDB.isPresent() && !subjectDB.get().getId().equals(subjectDto.getId());
    }

    public boolean isSubjectInEvaluation(String subjectId) {
        return evaluationRepository.findFirstBySubject(subjectId) != null;
    }

    public boolean isStudentInCourse(String courseId, String studentId) {
        return courseRepository.findByIdAndStudentOptional(courseId, studentId).isPresent();
    }

}
