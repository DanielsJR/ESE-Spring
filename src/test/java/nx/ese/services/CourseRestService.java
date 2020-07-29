package nx.ese.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.CourseController;
import nx.ese.documents.core.CourseName;
import nx.ese.dtos.CourseDto;
import nx.ese.dtos.UserDto;

@Service
public class CourseRestService {

    @Autowired
    private RestService restService;

    @Getter
    @Setter
    private CourseDto courseDto;

    @Getter
    @Setter
    private CourseDto courseDto2;

    @Autowired
    private UserRestService userRestService;

    private static final Logger logger = LoggerFactory.getLogger(CourseRestService.class);

    public void createCoursesDto() {
        userRestService.createUsersDto();

        restService.loginManager();
        userRestService.postTeacher();
        userRestService.postTeacher2();
        userRestService.postStudent();
        userRestService.postStudent2();

        List<UserDto> students = new ArrayList<>();
        students.add(userRestService.getStudentDto());

        List<UserDto> students2 = new ArrayList<>();
        students2.add(userRestService.getStudentDto2());

        logger.info("****************************CREATING_COURSES******************************************");
        this.courseDto = new CourseDto();
        this.courseDto.setName(CourseName.PRIMERO_H);
        this.courseDto.setYear("2018");
        this.courseDto.setChiefTeacher(userRestService.getTeacherDto());
        this.courseDto.setStudents(students);

        this.courseDto2 = new CourseDto();
        this.courseDto2.setName(CourseName.PRIMERO_G);
        this.courseDto2.setYear("2018");
        this.courseDto2.setChiefTeacher(userRestService.getTeacherDto2());
        this.courseDto2.setStudents(students2);
        logger.info("**************************************************************************************");
    }

    public void deleteCourses() {
        {
            logger.info("*********************************DELETING_COURSE***********************************");
            this.restService.loginManager();

            try {
                this.deleteCourse(this.courseDto.getId());
            } catch (Exception e) {
                logger.info("error: " + e.getMessage() + "courseDto: nothing to delete");
            }

            try {
                this.deleteCourse(this.courseDto2.getId());
            } catch (Exception e) {
                logger.info("error: " + e.getMessage() + "courseDto2: nothing to delete");
            }

            this.userRestService.deleteTeachers();
            this.userRestService.deleteStudents();

            logger.info("************************************************************************************");

        }

    }

    public void postCourse() {
        this.courseDto = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
                .path(CourseController.COURSE).bearerAuth(restService.getAuthToken().getToken()).body(this.courseDto)
                .post().build();
    }

    public void postCourse2() {
        this.courseDto2 = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
                .path(CourseController.COURSE).bearerAuth(restService.getAuthToken().getToken()).body(this.courseDto2)
                .post().build();
    }

    public void putCourse() {
        this.courseDto = restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
                .path(CourseController.COURSE).path(CourseController.PATH_ID).expand(courseDto.getId())
                .bearerAuth(restService.getAuthToken().getToken()).body(this.courseDto).put().build();

    }

    public void patchCourse() {
        // TODO Auto-generated method stub

    }

    public void deleteCourse(String id) {
        restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class).path(CourseController.COURSE)
                .path(CourseController.PATH_ID).expand(id).bearerAuth(restService.getAuthToken().getToken()).delete()
                .build();
    }

    public CourseDto getCourseByNameAndYear(CourseName name, String year) {
        return restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
                .path(CourseController.COURSE)
                .path(CourseController.NAME)
                .path(CourseController.PATH_NAME).expand(name)
                .path(CourseController.PATH_YEAR).expand(year)
                .bearerAuth(restService.getAuthToken().getToken()).get()
                .build();
    }

    public CourseDto getCourseById(String id) {
        return restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
                .path(CourseController.COURSE).path(CourseController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken()).get().build();

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<CourseDto> getFullCoursesByYear(String year) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(CourseController.COURSE)
                .path(CourseController.YEAR).path(CourseController.PATH_YEAR).expand(year)
                .bearerAuth(restService.getAuthToken().getToken()).get().build();
    }

    public CourseDto getCourseByChiefTeacherNameAndYear(String teacherName, String year) {
        return restService.restBuilder(new RestBuilder<CourseDto>()).clazz(CourseDto.class)
                .path(CourseController.COURSE).path(CourseController.TEACHER_NAME)
                .path(CourseController.PATH_TEACHER_NAME).expand(teacherName).path(CourseController.PATH_YEAR)
                .expand(year).bearerAuth(restService.getAuthToken().getToken()).get().build();

    }

}
