package nx.ese.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nx.ese.documents.User;
import nx.ese.documents.core.Evaluation;
import nx.ese.documents.core.Grade;
import nx.ese.documents.core.QuizStudent;
import nx.ese.dtos.EvaluationDto;
import nx.ese.dtos.GradeDto;
import nx.ese.repositories.EvaluationRepository;
import nx.ese.repositories.GradeRepository;
import nx.ese.repositories.QuizStudentRepository;
import nx.ese.repositories.UserRepository;

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
        grade.setStudent(this.setStudent(gradeDto));
        grade.setEvaluation(this.setEvaluation(gradeDto));
        if (gradeDto.getQuizStudent() != null) {
            grade.setQuizStudent(this.setQuizStudent(gradeDto));
        }
        return grade;
    }

    private User setStudent(GradeDto gradeDto) {
        return userRepository.findById(gradeDto.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("StudentNotFound"));
    }

    private Evaluation setEvaluation(GradeDto gradeDto) {
        return evaluationRepository.findById(gradeDto.getEvaluation().getId())
                .orElseThrow(() -> new RuntimeException("EvaluationNotFound"));
    }

    private QuizStudent setQuizStudent(GradeDto gradeDto) {
        return quizStudentRepository.findById(gradeDto.getQuizStudent().getId())
                .orElseThrow(() -> new RuntimeException("QuizStudentNotFound"));
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

    public boolean isGradeNull(GradeDto gradeDto) {
        return gradeDto.getGrade() == null;
    }

    public boolean isGradeRepeated(@Valid GradeDto gradeDto) {
        if (isStudentNull(gradeDto) || isEvaluationNull(gradeDto) || isGradeNull(gradeDto))
            return false;

        GradeDto gradeDB = gradeRepository.findByStudentAndEvaluation(gradeDto.getStudent().getId(),
                gradeDto.getEvaluation().getId());
        return gradeDB != null && !gradeDB.getId().equals(gradeDto.getId());
    }

    // CRUD******************************
    public GradeDto createGrade(@Valid GradeDto gradeDto) {
        Grade grade = new Grade();
        return new GradeDto(gradeRepository.insert(setGradeFromDto(grade, gradeDto)));
    }

    public Optional<GradeDto> modifyGrade(String id, @Valid GradeDto gradeDto) {
        Optional<Grade> grade = gradeRepository.findById(id);
        return grade.map(value -> new GradeDto(gradeRepository.save(setGradeFromDto(value, gradeDto))));
    }

    public Optional<GradeDto> deleteGrade(String id) {
        Optional<Grade> grade = gradeRepository.findById(id);
        if (grade.isPresent()) {
            gradeRepository.deleteById(id);
            return grade.map(GradeDto::new);
        }
        return Optional.empty();
    }

    public Optional<List<GradeDto>> getFullGrades() {
        List<GradeDto> list = gradeRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .stream()
                .map(GradeDto::new)
                .collect(Collectors.toList());
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<GradeDto> getGradeById(String gradeId) {
        return gradeRepository.findByIdOptionalDto(gradeId);
    }

    public Optional<List<GradeDto>> getGradesBySubject(String subjectId) {
        Optional<List<EvaluationDto>> evaluationList = this.evaluationRepository.findBySubject(subjectId);

        List<GradeDto> list = evaluationList.orElseThrow(NoSuchElementException::new)
                .stream()
                .map(e -> gradeRepository.findByEvaluation(e.getId()).orElseThrow(NoSuchElementException::new))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<List<GradeDto>> getTeacherGradesBySubject(String subjectId, String username) {
        Optional<List<EvaluationDto>> evaluationList = this.evaluationRepository.findBySubject(subjectId);

        List<GradeDto> gradeList = evaluationList.orElseThrow(NoSuchElementException::new)
                .stream()
                .filter(e -> e.getSubject().getTeacher().getUsername().equals(username))
                .map(e -> gradeRepository.findByEvaluation(e.getId()).orElseThrow(NoSuchElementException::new))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (gradeList.isEmpty())
            return Optional.empty();

        return Optional.of(gradeList);
    }

    public Optional<List<GradeDto>> getStudentGradesBySubject(String subjectId, String username) {
        Optional<List<EvaluationDto>> evaluationList = this.evaluationRepository.findBySubject(subjectId);

        List<GradeDto> gradeList = evaluationList.orElseThrow(NoSuchElementException::new)
                .stream()
                .map(e -> gradeRepository.findByEvaluation(e.getId()).orElseThrow(NoSuchElementException::new))
                .flatMap(List::stream)
                .filter(gs -> gs.getStudent().getUsername().equals(username))
                .collect(Collectors.toList());

        if (gradeList.isEmpty())
            return Optional.empty();

        return Optional.of(gradeList);
    }


}
