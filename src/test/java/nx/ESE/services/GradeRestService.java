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
	private CourseRestService courseRestService;

	@Autowired
	private SubjectRestService subjectRestService;

	private GradeDto gradeDto;
	
	private List<GradeDto> listGradeDto;

	private static final Logger logger = LoggerFactory.getLogger(GradeRestService.class);
	
	
	public GradeDto getGradeDto() {
		return gradeDto;
	}

	public void setGradeDto(GradeDto gradeDto) {
		this.gradeDto = gradeDto;
	}
	
	

	public List<GradeDto> getListGradeDto() {
		return listGradeDto;
	}

	public void setListGradeDto(List<GradeDto> listGradeDto) {
		this.listGradeDto = listGradeDto;
	}

	public void createGradeDto() {
		logger.warn(
				"*********************************CREAING_GRADE**************************************************");
		subjectRestService.createSubjectDto();
		subjectRestService.postSubject();

		this.gradeDto = new GradeDto();
		this.gradeDto.setType("prueba");
		this.gradeDto.setTitle("prueba-01");
		Date date = new Date();
		this.gradeDto.setDate(date);
		this.gradeDto.setStudent(userRestService.getStudentDto());
		this.gradeDto.setSubject(subjectRestService.getSubjectDto());
		this.gradeDto.setGrade(6.5);
		
		logger.warn("***********************************************************************************************");

	}

	public void deleteGrades() {
		logger.warn(
				"*********************************DELETING_GRADE**************************************************");
		this.restService.loginTeacher();

		try {
			String id = this.gradeDto.getId();
			this.restService.restBuilder().path(GradeController.GRADE).path(GradeController.PATH_ID).expand(id)
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "gradeDto: nothing to delete");
		}

		this.subjectRestService.deleteSubjects();

		logger.warn("***********************************************************************************************");

	}

	// POST********************************
	public GradeDto postGrade() {
		return	gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE)
				.bearerAuth(restService.getAuthToken().getToken())
				.body(gradeDto)
				.post()
				.build();
	}

	// PUT********************************
	public GradeDto putGrade(String id) {
		return	gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).path(GradeController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken())
				.body(gradeDto)
				.put()
				.build();
	}
	// PACH********************************

	// GET********************************
	public GradeDto getGradeById(String id) {
		return	gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
				.path(GradeController.GRADE).path(GradeController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken())
				.body(gradeDto)
				.get()
				.build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GradeDto> getFullGrades() {
		return	listGradeDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(GradeController.GRADE)
				.bearerAuth(restService.getAuthToken().getToken())
				.body(gradeDto)
				.get()
				.build();
	}
}
