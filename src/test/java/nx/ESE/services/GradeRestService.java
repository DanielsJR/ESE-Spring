package nx.ESE.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nx.ESE.controllers.GradeController;
import nx.ESE.dtos.GradeDto;

@Service
public class GradeRestService {

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Autowired
	private SubjectRestService subjectRestService;

	private GradeDto gradeDto;

	private GradeDto gradeDto2;

	private List<GradeDto> listGradeDto;

	private static final Logger logger = LoggerFactory.getLogger(GradeRestService.class);

	public GradeDto getGradeDto() {
		return gradeDto;
	}

	public void setGradeDto(GradeDto gradeDto) {
		this.gradeDto = gradeDto;
	}

	public GradeDto getGradeDto2() {
		return gradeDto2;
	}

	public void setGradeDto2(GradeDto gradeDto2) {
		this.gradeDto2 = gradeDto2;
	}

	public List<GradeDto> getListGradeDto() {
		return listGradeDto;
	}

	public void setListGradeDto(List<GradeDto> listGradeDto) {
		this.listGradeDto = listGradeDto;
	}

	public void createGradesDto() {

		logger.warn(
				"*********************************CREATING_GRADES**************************************************");

		restService.loginManager();
		subjectRestService.createSubjectsDto();
		subjectRestService.postSubject();

		this.gradeDto = new GradeDto();
		this.gradeDto.setType("prueba");
		this.gradeDto.setTitle("prueba-01");
		Date date = new Date();
		this.gradeDto.setDate(date);
		this.gradeDto.setStudent(userRestService.getStudentDto());
		this.gradeDto.setSubject(subjectRestService.getSubjectDto());
		this.gradeDto.setGrade(6.5);

		this.gradeDto2 = new GradeDto();
		this.gradeDto2.setType("prueba");
		this.gradeDto2.setTitle("prueba-02");
		Date date2 = new Date();
		this.gradeDto2.setDate(date2);
		this.gradeDto2.setStudent(userRestService.getStudentDto2());
		this.gradeDto2.setSubject(subjectRestService.getSubjectDto());
		this.gradeDto2.setGrade(3.5);

		logger.warn("***********************************************************************************************");

	}

	public void deleteGrades() {
		logger.warn(
				"*********************************DELETING_GRADE**************************************************");
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

		this.subjectRestService.deleteSubjects();

		logger.warn("***********************************************************************************************");

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
		return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).path(GradeController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).delete().build();
	}

	// GET
	public GradeDto getGradeById(String id) {
		return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).path(GradeController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).body(gradeDto).get().build();
	}

	//
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GradeDto> getFullGrades() {
		return listGradeDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(GradeController.GRADE).bearerAuth(restService.getAuthToken().getToken()).get().build();
	}

}
