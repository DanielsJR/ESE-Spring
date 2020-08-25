package nx.ese.services;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import nx.ese.dtos.validators.NxPattern;
import org.springframework.beans.factory.annotation.Autowired;
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

        Optional<EvaluationDto> evaluationDB = evaluationRepository.findByTitleAndTypeAndSubjectAndDate(evaluationDto.getTitle(),
                evaluationDto.getType(), evaluationDto.getSubject().getId(), evaluationDto.getDate());
        return evaluationDB.isPresent() && !evaluationDB.get().getId().equals(evaluationDto.getId());
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

    public boolean isDateValid(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(NxPattern.LOCAL_DATE_FORMAT);
        try {
            LocalDate.parse(date.toString(), dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private Evaluation setEvaluationFromDto(Evaluation evaluation, @Valid EvaluationDto evaluationDto) {
        evaluation.setType(evaluationDto.getType());
        evaluation.setTitle(evaluationDto.getTitle());
        evaluation.setSubject(this.setSubject(evaluationDto));
        if (evaluationDto.getQuiz() != null) evaluation.setQuiz(this.setQuiz(evaluationDto));
        evaluation.setDate(evaluationDto.getDate());
        evaluation.setOpen(evaluationDto.getOpen());
        return evaluation;
    }

    private Quiz setQuiz(EvaluationDto evaluationDto) {
        return quizRepository.findById(evaluationDto.getQuiz().getId())
                .orElseThrow(NoSuchElementException::new);
    }

    private Subject setSubject(EvaluationDto evaluationDto) {
        return subjectRepository.findById(evaluationDto.getSubject()
                .getId()).orElseThrow(NoSuchElementException::new);
    }

    // CRUD******************************
    public EvaluationDto createEvaluation(@Valid EvaluationDto evaluationDto) {
        Evaluation evaluation = setEvaluationFromDto(new Evaluation(), evaluationDto);
        evaluation.setOpen(true);
        return new EvaluationDto(evaluationRepository.insert(evaluation));
    }

    public Optional<EvaluationDto> modifyEvaluation(String id, @Valid EvaluationDto evaluationDto) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        return evaluation.map(e -> new EvaluationDto(evaluationRepository.save(setEvaluationFromDto(e, evaluationDto))));
    }

    public Optional<EvaluationDto> deleteEvaluation(String id) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        if (evaluation.isPresent()) {
            evaluationRepository.deleteById(id);
            return evaluation.map(EvaluationDto::new);
        }
        return Optional.empty();
    }

    public Optional<List<EvaluationDto>> getEvaluationsBySubject(String subjectId) {
        return evaluationRepository.findBySubject(subjectId);
    }

    public Optional<EvaluationDto> getEvaluationById(String id) {
        return evaluationRepository.findByIdOptionalDto(id);
    }

    public Optional<EvaluationDto> getEvaluationBySubjectAndDate(String subjectId, LocalDate date) {
        return evaluationRepository.findBySubjectAndDate(subjectId, date);
    }


}
