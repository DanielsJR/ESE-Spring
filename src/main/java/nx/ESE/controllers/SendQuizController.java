package nx.ESE.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javassist.expr.NewArray;
import nx.ESE.documents.core.CorrespondItem;
import nx.ESE.documents.core.Grade;
import nx.ESE.documents.core.IncompleteTextItem;
import nx.ESE.documents.core.MultipleSelectionItem;
import nx.ESE.documents.core.TrueFalseItem;
import nx.ESE.dtos.CourseDto;
import nx.ESE.dtos.EvaluationDto;
import nx.ESE.dtos.GradeDto;
import nx.ESE.dtos.MessageDto;
import nx.ESE.dtos.QuizStudentDto;
import nx.ESE.dtos.SubjectDto;
import nx.ESE.dtos.UserDto;
import nx.ESE.exceptions.DocumentAlreadyExistException;
import nx.ESE.exceptions.DocumentNotFoundException;
import nx.ESE.exceptions.FieldInvalidException;
import nx.ESE.exceptions.FieldNotFoundException;
import nx.ESE.services.EvaluationService;
import nx.ESE.services.GradeService;
import nx.ESE.services.UserService;

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
	public GradeDto getQuizNotification(@DestinationVariable String cId, String eId)
			throws FieldInvalidException, DocumentAlreadyExistException {

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
			@DestinationVariable String sId, GradeDto gradeDto) throws FieldNotFoundException {

		EvaluationDto eDto = evaluationService.getEvaluationById(eId).orElseThrow(() -> new FieldNotFoundException("eId"));
		gradeDto.setEvaluation(eDto);

		if (!this.userService.existsUserId(sId))
			throw new FieldNotFoundException("sId");
		UserDto sDto = userService.getUserById(sId);
		gradeDto.setStudent(sDto);
		
		//TODO calculate
		gradeDto.setGrade(7.0);

		return gradeDto;
	}

	@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
	@MessageMapping("/grade-to-student/student/{sId}/grade/{gId}")
	@SendTo("/topic/grade-to-student/student/{sId}")
	public GradeDto gradeNotificationToStudent(@DestinationVariable String sId, @DestinationVariable String gId) throws DocumentNotFoundException {
		
		GradeDto gDto = this.gradeService.getGradeById(gId).orElseThrow(() -> new DocumentNotFoundException("Nota"));
		
		return gDto;
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