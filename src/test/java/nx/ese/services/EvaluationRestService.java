package nx.ese.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.EvaluationController;
import nx.ese.documents.core.EvaluationType;
import nx.ese.dtos.EvaluationDto;

@Service
public class EvaluationRestService {

	@Autowired
	private RestService restService;

	@Autowired
	private SubjectRestService subjectRestService;

	@Getter
	@Setter
	private EvaluationDto evaluationDto;

	@Getter
	@Setter
	private EvaluationDto evaluationDto2;

	@Getter
	@Setter
	private List<EvaluationDto> listEvaluationDto;

	private static final Logger logger = LoggerFactory.getLogger(GradeRestService.class);

	public void createEvaluationsDto() {
		subjectRestService.createSubjectsDto();
		
		restService.loginManager();
		subjectRestService.postSubject();

		logger.warn("*****************************CREATING_EVALUATIONS********************************************");
		this.evaluationDto = new EvaluationDto();
		this.evaluationDto.setType(EvaluationType.DISERTACION);
		this.evaluationDto.setTitle("Test de evaluación-01");
		Date date = new Date();
		this.evaluationDto.setDate(date);
		this.evaluationDto.setSubject(subjectRestService.getSubjectDto());
		// this.evaluationDto.setQuiz(quiz);

		this.evaluationDto2 = new EvaluationDto();
		this.evaluationDto2.setType(EvaluationType.PRUEBA);
		this.evaluationDto2.setTitle("Test de evaluación-02");
		Date date2 = new Date();
		this.evaluationDto2.setDate(date2);
		this.evaluationDto2.setSubject(subjectRestService.getSubjectDto());
		// this.evaluationDto2.setQuiz(quiz);
		logger.warn("*****************************************************************************************");
	}

	public void deleteEvaluationsDto() {
		logger.warn("*****************************DELETING_EVALUATIONS********************************************");

		this.restService.loginTeacher();

		try {
			this.deleteEvaluation(this.getEvaluationDto().getId());
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "evaluationDto: nothing to delete");
		}

		try {
			this.deleteEvaluation(this.getEvaluationDto2().getId());
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "evaluationDto2: nothing to delete");
		}

		this.subjectRestService.deleteSubjects();

		logger.warn("***************************************************************************************");

	}

	// POST
	public EvaluationDto postEvaluation() {
		return evaluationDto = restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
				.path(EvaluationController.EVALUATION).bearerAuth(restService.getAuthToken().getToken())
				.body(evaluationDto).post().build();

	}

	public EvaluationDto postEvaluation2() {
		return evaluationDto2 = restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
				.path(EvaluationController.EVALUATION).bearerAuth(restService.getAuthToken().getToken())
				.body(evaluationDto2).post().build();

	}

	// PUT
	public EvaluationDto putEvaluation() {
		return evaluationDto = restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
				.path(EvaluationController.EVALUATION).path(EvaluationController.PATH_ID).expand(evaluationDto.getId())
				.bearerAuth(restService.getAuthToken().getToken()).body(evaluationDto).put().build();

	}

	// DELETE
	public EvaluationDto deleteEvaluation(String id) {
		return restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
				.path(EvaluationController.EVALUATION).path(EvaluationController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).delete().build();

	}

	// GET
	public EvaluationDto getEvaluationById(String id) {
		return restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
				.path(EvaluationController.EVALUATION).path(EvaluationController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).get().build();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EvaluationDto> getFullEvaluations() {
		return listEvaluationDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(EvaluationController.EVALUATION).bearerAuth(restService.getAuthToken().getToken()).get().build();

	}
}
