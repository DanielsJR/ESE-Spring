package nx.ese.controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import nx.ese.documents.core.CorrespondItem;
import nx.ese.documents.core.IncompleteTextItem;
import nx.ese.documents.core.MultipleSelectionItem;
import nx.ese.documents.core.TrueFalseItem;
import nx.ese.dtos.EvaluationDto;
import nx.ese.dtos.GradeDto;
import nx.ese.dtos.MessageDto;
import nx.ese.dtos.QuizStudentDto;
import nx.ese.dtos.UserDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.services.EvaluationService;
import nx.ese.services.GradeService;
import nx.ese.services.UserService;

@RestController
public class SendQuizController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('TEACHER')")
    @MessageMapping("/send-quiz/course/{cId}")
    @SendTo("/topic/send-quiz/course/{cId}")
    public GradeDto getQuizNotification(@DestinationVariable String cId, String eId) {

        //TODO service
        EvaluationDto eDto = evaluationService.getEvaluationById(eId).get();

        QuizStudentDto qsDto = new QuizStudentDto(eDto.getQuiz());

        List<CorrespondItem> correspondItems = qsDto.getCorrespondItems();
        List<String> corresponds = correspondItems.stream().map(item -> item.getCorrespond())
                .collect(Collectors.toList());
        Collections.shuffle(corresponds);

        int i = 0;
        for (CorrespondItem co : correspondItems) {
            co.setCorrespond(corresponds.toArray()[i].toString());
            i++;
        }

        qsDto.setCorrespondItems(correspondItems);

        List<TrueFalseItem> trueFalseItems = qsDto.getTrueFalseItems();
        trueFalseItems.stream().forEach(item -> item.setAnswer(false));

        List<MultipleSelectionItem> multipleSelectionItems = qsDto.getMultipleSelectionItems();
        multipleSelectionItems.stream().forEach(item -> item.setAnswer(null));

        List<IncompleteTextItem> incompleteTextItems = qsDto.getIncompleteTextItems();
        incompleteTextItems.stream().forEach(item -> item.setAnswer(null));

        GradeDto gradeDto = new GradeDto();
        gradeDto.setEvaluation(eDto);
        gradeDto.setGrade(0);
        gradeDto.setQuizStudent(qsDto);

        // String course = eDto.getSubject().getCourse().getName().getCode();
        // template.convertAndSend("/topic/send-quiz/" + course, grade);

        return gradeDto;

    }

    // @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    @MessageMapping("/grade-to-teacher/course/{cId}/evaluation/{eId}/student/{sId}")
    @SendTo("/topic/grade-to-teacher/course/{cId}")
    public GradeDto gradeNotificationToTeacher(@DestinationVariable String cId, @DestinationVariable String eId,
                                               @DestinationVariable String sId, GradeDto gradeDto) throws FieldNotFoundException, DocumentNotFoundException {

        EvaluationDto eDto = evaluationService.getEvaluationById(eId).orElseThrow(() -> new FieldNotFoundException("eId"));
        gradeDto.setEvaluation(eDto);

        UserDto sDto = userService.getUserById(sId).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
        gradeDto.setStudent(sDto);

        //TODO calculate
        gradeDto.setGrade(7.0);

        return gradeDto;
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    @MessageMapping("/grade-to-student/student/{sId}/grade/{gId}")
    @SendTo("/topic/grade-to-student/student/{sId}")
    public GradeDto gradeNotificationToStudent(@DestinationVariable String sId, @DestinationVariable String gId) throws DocumentNotFoundException {
        return this.gradeService.getGradeById(gId).orElseThrow(() -> new DocumentNotFoundException("Nota"));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    @MessageMapping("/notification-test")
    public void notificationTest(MessageDto message) {
        message.getNotification().increment();
        template.convertAndSend("/topic/notification-test", message);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    @MessageMapping("/cualquier-test")
    public void cualquierTest(UserDto message) {
        template.convertAndSend("/topic/cualquier-test", message);
    }
}