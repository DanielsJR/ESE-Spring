package nx.ese.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import nx.ese.dtos.GradeDto;
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

    // Exceptions*********************
    public boolean existsById(String id) {
        return quizStudentRepository.existsById(id);
    }

    public boolean isIdNull(@Valid QuizStudentDto quizStudentDto) {
        return quizStudentDto.getId() == null;
    }

    public boolean isQuizStudentRepeated(@Valid QuizStudentDto quizStudentDto) {
        QuizStudentDto quizStudentDB = this.quizStudentRepository
                .findByCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
                        quizStudentDto.getCorrespondItems(), quizStudentDto.getTrueFalseItems(),
                        quizStudentDto.getMultipleSelectionItems(), quizStudentDto.getIncompleteTextItems());

        return quizStudentDB != null && !quizStudentDB.getId().equals(quizStudentDto.getId());
    }

    public boolean isQuizStudentInGrade(String id) {
        return gradeRepository.findByQuizStudent(id).isPresent();
    }

    public boolean isTeacherInQuizStudent(String quizStudentId, String teacherUsername) {
        Optional<GradeDto> gDto = gradeRepository.findByQuizStudent(quizStudentId);
        return gDto.isPresent() && gDto.get().getEvaluation().getSubject().getTeacher().getUsername().equals(teacherUsername);
    }

    // CRUD******************************
    public QuizStudentDto createQuizStudent(@Valid QuizStudentDto quizStudentDto) {
        QuizStudent quizStudent = new QuizStudent();
        return new QuizStudentDto(quizStudentRepository.insert(setQuizStudentFromDto(quizStudent, quizStudentDto)));
    }

    public Optional<QuizStudentDto> modifyQuizStudent(String quizStudentId, @Valid QuizStudentDto quizStudentDto) {
        return quizStudentRepository.findById(quizStudentId).map(qs -> new QuizStudentDto(quizStudentRepository.save(setQuizStudentFromDto(qs, quizStudentDto))));

    }

    public Optional<QuizStudentDto> deleteQuizStudent(String quizStudentid) {
        Optional<QuizStudent> quizStudent = quizStudentRepository.findById(quizStudentid);
        if (quizStudent.isPresent()) {
            quizStudentRepository.deleteById(quizStudentid);
            return quizStudent.map(QuizStudentDto::new);
        }
        return Optional.empty();
    }

    public Optional<QuizStudentDto> getQuizStudentById(String id) {
        Optional<QuizStudent> quizStudent = quizStudentRepository.findById(id);
        return quizStudent.map(QuizStudentDto::new);
    }


}
