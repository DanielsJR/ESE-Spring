package nx.ese.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.QuizStudentController;
import nx.ese.documents.core.CorrespondItem;
import nx.ese.documents.core.IncompleteTextItem;
import nx.ese.documents.core.MultipleSelectionItem;
import nx.ese.documents.core.TrueFalseItem;
import nx.ese.dtos.QuizStudentDto;

@Service
public class QuizStudentRestService {

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Autowired
	private SubjectRestService subjectRestService;

	@Getter
	@Setter
	private QuizStudentDto quizStudentDto;

	@Getter
	@Setter
	private QuizStudentDto quizStudentDto2;
	
    @Getter
	@Setter
	private List<QuizStudentDto> listQuizStudentDto;

	private static final Logger logger = LoggerFactory.getLogger(QuizStudentRestService.class);

	public void createQuizStudentsDto() {
		subjectRestService.createSubjectsDto();
		
		restService.loginManager();
		subjectRestService.postSubject();

		logger.warn("*********************************CREATING_QUIZES-STUDENT*****************************************");
		this.quizStudentDto = new QuizStudentDto();
		Date date = new Date();
		
		List<CorrespondItem> correspondItems = new ArrayList<CorrespondItem>();
		correspondItems.add(new CorrespondItem("", ""));
		correspondItems.add(new CorrespondItem("", ""));
		this.quizStudentDto.setCorrespondItems(correspondItems);
			
		List<TrueFalseItem> trueFalseItems = new ArrayList<TrueFalseItem>();
		trueFalseItems.add(new TrueFalseItem("", true));
		trueFalseItems.add(new TrueFalseItem("", true));
		this.quizStudentDto.setTrueFalseItems(trueFalseItems);
			
		List<MultipleSelectionItem> multipleSelectionItems = new ArrayList<MultipleSelectionItem>();
		multipleSelectionItems.add(new MultipleSelectionItem("", "", "", "", "", ""));
		multipleSelectionItems.add(new MultipleSelectionItem("", "", "", "", "", ""));
		this.quizStudentDto.setMultipleSelectionItems(multipleSelectionItems);
			
		List<IncompleteTextItem> incompleteTextItems = new ArrayList<IncompleteTextItem>();
		incompleteTextItems.add(new IncompleteTextItem("", ""));
		incompleteTextItems.add(new IncompleteTextItem("", ""));
		this.quizStudentDto.setIncompleteTextItems(incompleteTextItems);
		
		this.quizStudentDto2 = new QuizStudentDto();
		Date date2 = new Date();
		
		List<CorrespondItem> correspondItems2 = new ArrayList<CorrespondItem>();
		correspondItems2.add(new CorrespondItem("2", "2"));
		correspondItems2.add(new CorrespondItem("2", "2"));
		this.quizStudentDto2.setCorrespondItems(correspondItems2);
			
		List<TrueFalseItem> trueFalseItems2 = new ArrayList<TrueFalseItem>();
		trueFalseItems2.add(new TrueFalseItem("2", true));
		trueFalseItems2.add(new TrueFalseItem("2", true));
		this.quizStudentDto2.setTrueFalseItems(trueFalseItems2);
			
		List<MultipleSelectionItem> multipleSelectionItems2 = new ArrayList<MultipleSelectionItem>();
		multipleSelectionItems2.add(new MultipleSelectionItem("2", "", "", "", "", ""));
		multipleSelectionItems2.add(new MultipleSelectionItem("2", "", "", "", "", ""));
		this.quizStudentDto2.setMultipleSelectionItems(multipleSelectionItems2);
			
		List<IncompleteTextItem> incompleteTextItems2 = new ArrayList<IncompleteTextItem>();
		incompleteTextItems2.add(new IncompleteTextItem("2", ""));
		incompleteTextItems2.add(new IncompleteTextItem("2", ""));
		this.quizStudentDto2.setIncompleteTextItems(incompleteTextItems2);

		
		
		
		logger.warn("**************************************************************************************");

	}

	public void deleteQuizStudentsDto() {
		logger.warn("*********************************DELETING_QUIZES-STUDENT**************************************");
		this.restService.loginTeacher();

		try {
			this.deleteQuizStudent(this.getQuizStudentDto().getId());
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "quizStudentDto: nothing to delete");
		}

		try {
			this.deleteQuizStudent(this.getQuizStudentDto2().getId());
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "quizStudentDto2: nothing to delete");
		}

		this.subjectRestService.deleteSubjects();

		logger.warn("********************************************************************************");

	}

	// POST
	public QuizStudentDto postQuizStudent() {
		return quizStudentDto = restService.restBuilder(new RestBuilder<QuizStudentDto>()).clazz(QuizStudentDto.class)
				.path(QuizStudentController.QUIZ_STUDENT)
				.bearerAuth(restService.getAuthToken().getToken())
				.body(quizStudentDto)
				.post()
				.build();
	}

	public QuizStudentDto postQuizStudent2() {
		return quizStudentDto2 = restService.restBuilder(new RestBuilder<QuizStudentDto>()).clazz(QuizStudentDto.class)
				.path(QuizStudentController.QUIZ_STUDENT)
				.bearerAuth(restService.getAuthToken().getToken())
				.body(quizStudentDto2)
				.post()
				.build();

	}

	// PUT
	public QuizStudentDto putQuizStudent() {
		return quizStudentDto = restService.restBuilder(new RestBuilder<QuizStudentDto>()).clazz(QuizStudentDto.class)
				.path(QuizStudentController.QUIZ_STUDENT).path(QuizStudentController.PATH_ID).expand(quizStudentDto.getId())
				.bearerAuth(restService.getAuthToken().getToken()).body(quizStudentDto).put().build();
	}

	// DELETE
	public QuizStudentDto deleteQuizStudent(String id) {
		return restService.restBuilder(new RestBuilder<QuizStudentDto>()).clazz(QuizStudentDto.class).path(QuizStudentController.QUIZ_STUDENT)
				.path(QuizStudentController.PATH_ID).expand(id).bearerAuth(restService.getAuthToken().getToken()).delete()
				.build();
	}

	// GET
	public QuizStudentDto getQuizStudentById(String id) {
		return restService.restBuilder(new RestBuilder<QuizStudentDto>()).clazz(QuizStudentDto.class).path(QuizStudentController.QUIZ_STUDENT)
				.path(QuizStudentController.PATH_ID).expand(id).bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<QuizStudentDto> getFullQuizStudents() {
		return listQuizStudentDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(QuizStudentController.QUIZ_STUDENT).bearerAuth(restService.getAuthToken().getToken()).get().build();
	}

}
