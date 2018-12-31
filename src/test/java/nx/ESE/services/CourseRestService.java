package nx.ESE.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nx.ESE.controllers.CourseController;
import nx.ESE.documents.core.CourseName;
import nx.ESE.dtos.CourseDto;
import nx.ESE.dtos.UserDto;

@Service
public class CourseRestService {

	@Autowired
	private RestService restService;

	private CourseDto courseDto;
	
	private List<CourseDto> listCoursesDto;

	@Autowired
	private UserRestService userRestService;

	private static final Logger logger = LoggerFactory.getLogger(CourseRestService.class);

	public CourseDto getCourseDto() {
		return courseDto;
	}

	public void setCourseDto(CourseDto courseDto) {
		this.courseDto = courseDto;
	}
	
	

	public List<CourseDto> getListCoursesDto() {
		return listCoursesDto;
	}

	public void setListCoursesDto(List<CourseDto> listCoursesDto) {
		this.listCoursesDto = listCoursesDto;
	}

	public void createCourseDto() {
		logger.warn(
				"*********************************CREATING_COURSE**************************************************");
		
		restService.loginManager();

		userRestService.createUserDtos();
		userRestService.postTeacher();
		userRestService.postStudent();
		userRestService.postStudent2();

		List<UserDto> students = new ArrayList<>();
		students.add(userRestService.getStudentDto());
		students.add(userRestService.getStudentDto2());

		this.courseDto = new CourseDto();
		this.courseDto.setName(CourseName.PRIMERO_H);
		this.courseDto.setYear(2018);
		this.courseDto.setChiefTeacher(userRestService.getTeacherDto());
		this.courseDto.setStudents(students);
		
		logger.warn(
				"***********************************************************************************************");
	}

	public void deleteCourses() {
		{
			logger.warn(
					"*********************************DELETING_COURSE**************************************************");
			this.restService.loginManager();

			try {
				this.restService.restBuilder().path(CourseController.COURSE).path(CourseController.PATH_ID)
						.expand(this.courseDto.getId()).bearerAuth(restService.getAuthToken().getToken()).delete()
						.build();
			} catch (Exception e) {
				logger.warn("error: " + e.getMessage() + "courseDto: nothing to delete");
			}

			this.userRestService.deleteTeachers();
			this.userRestService.deleteStudents();

			logger.warn(
					"***********************************************************************************************");

		}

	}

	// POST********************************
	public void postCourse() {
		courseDto = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
				.path(CourseController.COURSE).bearerAuth(restService.getAuthToken().getToken()).body(courseDto).post()
				.build();
	}

	// PUT********************************
	public void putCourse(String id) {
		courseDto = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
				.path(CourseController.COURSE)
				.path(CourseController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).body(courseDto).put()
				.build();

	}

	// PACH********************************
	public void patchCourse() {
		// TODO Auto-generated method stub

	}

	// GET********************************
	public CourseDto getCourseByName(CourseName name, int year) {
		return courseDto = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
				.path(CourseController.COURSE).path(CourseController.NAME).path(CourseController.PATH_NAME).expand(name)
				.path(CourseController.PATH_YEAR).expand(year).bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
	}

	public CourseDto getCourseById(String id) {
		return courseDto = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
				.path(CourseController.COURSE)
				.path(CourseController.PATH_ID).expand(id)
				.bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CourseDto> getFullCoursesByYear(int year) {
	    return  listCoursesDto = restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
				.path(CourseController.COURSE)
				.path(CourseController.YEAR).path(CourseController.PATH_YEAR).expand(year)
				.bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
	}
	
	


}