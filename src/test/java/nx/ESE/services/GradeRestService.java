package nx.ESE.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ESE.controllers.GradeController;
import nx.ESE.dtos.GradeDto;

@Service
public class GradeRestService {

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Autowired
	private EvaluationRestService evaluationRestService;

	@Getter
	@Setter
	private GradeDto gradeDto;

	@Getter
	@Setter
	private GradeDto gradeDto2;

	@Getter
	@Setter
	private List<GradeDto> listGradeDto;

	private static final Logger logger = LoggerFactory.getLogger(GradeRestService.class);

	public void createGradesDto() {
		evaluationRestService.createEvaluationsDto();
		
		restService.loginTeacher();
		evaluationRestService.postEvaluation();

		logger.warn("*********************************CREATING_GRADES*****************************************");
		this.gradeDto = new GradeDto();
		this.gradeDto.setStudent(userRestService.getStudentDto());
		this.gradeDto.setEvaluation(evaluationRestService.getEvaluationDto());
		this.gradeDto.setGrade(6.5);

		this.gradeDto2 = new GradeDto();
		this.gradeDto2.setStudent(userRestService.getStudentDto2());
		this.gradeDto2.setEvaluation(evaluationRestService.getEvaluationDto());
		this.gradeDto2.setGrade(3.5);
		logger.warn("**************************************************************************************");

	}

	public void deleteGrades() {
		logger.warn("*********************************DELETING_GRADE**************************************");
		this.restService.loginTeacher();

		try {
			this.deleteGrade(this.getGradeDto().getId());
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "gradeDto: nothing to delete");
		}

		try {
			this.deleteGrade(this.getGradeDto2().getId());
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "gradeDto2: nothing to delete");
		}

		this.evaluationRestService.deleteEvaluationsDto();

		logger.warn("********************************************************************************");

	}

	// POST
	public GradeDto postGrade() {
		return gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).bearerAuth(restService.getAuthToken().getToken()).body(gradeDto).post()
				.build();
	}

	public GradeDto postGrade2() {
		return gradeDto2 = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).bearerAuth(restService.getAuthToken().getToken()).body(gradeDto2).post()
				.build();

	}

	// PUT
	public GradeDto putGrade() {
		return gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).path(GradeController.PATH_ID).expand(gradeDto.getId())
				.bearerAuth(restService.getAuthToken().getToken()).body(gradeDto).put().build();
	}

	// DELETE
	public GradeDto deleteGrade(String id) {
		return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class).path(GradeController.GRADE)
				.path(GradeController.PATH_ID).expand(id).bearerAuth(restService.getAuthToken().getToken()).delete()
				.build();
	}

	// GET
	public GradeDto getGradeById(String id) {
		return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE)
				.path(GradeController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GradeDto> getFullGrades() {
		return listGradeDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(GradeController.GRADE).bearerAuth(restService.getAuthToken().getToken()).get().build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GradeDto> getGradesBySubject(String id) {
		return listGradeDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(GradeController.GRADE)
				.path(GradeController.SUBJECT)
				.path(GradeController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken())
				.get()
				.build();
		
	}

}
