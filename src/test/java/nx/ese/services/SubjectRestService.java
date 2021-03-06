package nx.ese.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.SubjectController;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.SubjectDto;

@Service
public class SubjectRestService {

    @Autowired
    private RestService restService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private CourseRestService courseRestService;

    @Getter
    @Setter
    private SubjectDto subjectDto;

    @Getter
    @Setter
    private SubjectDto subjectDto2;

    private static final Logger logger = LoggerFactory.getLogger(SubjectRestService.class);


    public void createSubjectsDto() {
        courseRestService.createCoursesDto();

        restService.loginManager();
        courseRestService.postCourse();
        courseRestService.postCourse2();

        logger.info("***********************CREATING_SUBJECTS**********************************************");
        this.subjectDto = new SubjectDto();
        this.subjectDto.setName(SubjectName.MATEMATICAS);
        this.subjectDto.setTeacher(userRestService.getTeacherDto2());
        this.subjectDto.setCourse(courseRestService.getCourseDto());

        this.subjectDto2 = new SubjectDto();
        this.subjectDto2.setName(SubjectName.LENGUAJE);
        this.subjectDto2.setTeacher(userRestService.getTeacherDto());
        this.subjectDto2.setCourse(courseRestService.getCourseDto2());

        logger.info("****************************************************************************************");

    }

    public void deleteSubjects() {
        logger.info("*********************************DELETING_SUBJECT*****************************************");
        this.restService.loginManager();

        try {
            this.deleteSubject(this.subjectDto.getId());
        } catch (Exception e) {
            logger.info("subjectDto: nothing to delete");
        }

        try {
            this.deleteSubject(this.subjectDto2.getId());
        } catch (Exception e) {
            logger.info("subjectDto2: nothing to delete");
        }

        this.courseRestService.deleteCourses();

        logger.info("*******************************************************************************************");

    }

    public void postSubject() {
        this.subjectDto = restService.restBuilder(new RestBuilder<SubjectDto>()).clazz(SubjectDto.class)
                .path(SubjectController.SUBJECT)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(subjectDto)
                .post()
                .build();
    }

    public void postSubject2() {
        this.subjectDto2 = restService.restBuilder(new RestBuilder<SubjectDto>()).clazz(SubjectDto.class)
                .path(SubjectController.SUBJECT)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(subjectDto2)
                .post()
                .build();
    }

    public void putSubject() {
        this.subjectDto = restService.restBuilder(new RestBuilder<SubjectDto>()).clazz(SubjectDto.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.PATH_ID).expand(subjectDto.getId())
                .bearerAuth(restService.getAuthToken().getToken())
                .body(subjectDto)
                .put()
                .build();
    }

    public void deleteSubject(String id) {
        restService.restBuilder(new RestBuilder<SubjectDto>()).clazz(SubjectDto.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    public SubjectDto getSubjectById(String id) {
        return restService.restBuilder(new RestBuilder<SubjectDto>()).clazz(SubjectDto.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    public SubjectDto getSubjectByNameAndCourse(SubjectName name, String id) {
        return restService.restBuilder(new RestBuilder<SubjectDto>()).clazz(SubjectDto.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.NAME)
                .path(SubjectController.PATH_NAME).expand(name)
                .path(SubjectController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken()).get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SubjectDto> getSubjectsByYear(String year) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.YEAR)
                .path(SubjectController.PATH_YEAR).expand(year)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SubjectDto> getSubjectsByTeacherAndYear(String username, String year) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.TEACHER)
                .path(SubjectController.PATH_USERNAME).expand(username)
                .path(SubjectController.PATH_YEAR).expand(year)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SubjectDto> getStudentSubjectsByCourse(String courseId, String username) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(SubjectController.SUBJECT)
                .path(SubjectController.COURSE)
                .path(SubjectController.PATH_ID).expand(courseId)
                .path(SubjectController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }
}
