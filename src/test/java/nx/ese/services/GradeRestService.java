package nx.ese.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.GradeController;
import nx.ese.dtos.GradeDto;

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

    String teacherUsername;
    String teacherUsername2;

    private static final Logger logger = LoggerFactory.getLogger(GradeRestService.class);

    public void createGradesDto() {
        evaluationRestService.createEvaluationsDto();
        teacherUsername = evaluationRestService.getEvaluationDto().getSubject().getTeacher().getUsername();
        teacherUsername2 = evaluationRestService.getEvaluationDto2().getSubject().getTeacher().getUsername();


        restService.loginTeacher();
        evaluationRestService.postEvaluation();
        evaluationRestService.postEvaluation2();

        logger.info("*********************************CREATING_GRADES*****************************************");
        this.gradeDto = new GradeDto();
        this.gradeDto.setStudent(userRestService.getStudentDto());
        this.gradeDto.setEvaluation(evaluationRestService.getEvaluationDto());
        this.gradeDto.setGrade(6.5);

        this.gradeDto2 = new GradeDto();
        this.gradeDto2.setStudent(userRestService.getStudentDto2());
        this.gradeDto2.setEvaluation(evaluationRestService.getEvaluationDto2());
        this.gradeDto2.setGrade(3.5);
        logger.info("**************************************************************************************");

    }

    public void deleteGrades() {
        logger.info("*********************************DELETING_GRADE**************************************");

        this.restService.loginUser(teacherUsername, teacherUsername + "@ESE1");
        try {
            this.deleteGrade(gradeDto.getId(), teacherUsername);
        } catch (Exception e) {
            logger.info("gradeDto: nothing to delete");
        }

        this.restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        try {
            this.deleteGrade(gradeDto2.getId(), teacherUsername2);
        } catch (Exception e) {
            logger.info("gradeDto2: nothing to delete");
        }

        this.evaluationRestService.deleteEvaluationsDto();

        logger.info("********************************************************************************");

    }

    public GradeDto postGrade(String teacherUsername) {
        return gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
                .path(GradeController.GRADE)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(gradeDto)
                .post()
                .build();
    }

    public GradeDto postGrade2(String teacherUsername) {
        return gradeDto2 = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
                .path(GradeController.GRADE)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(gradeDto2)
                .post()
                .build();

    }

    public GradeDto putGrade(String teacherUsername) {
        return gradeDto = restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeDto.getId())
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(gradeDto)
                .put()
                .build();
    }

    public GradeDto deleteGrade(String id, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(id)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    public GradeDto getGradeById(String gradeId) {
        return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeId)
                .bearerAuth(restService.getAuthToken().getToken()).get()
                .build();
    }

    public GradeDto getTeacherGradeById(String gradeId, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<GradeDto>()).clazz(GradeDto.class)
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeId)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<GradeDto> getGradesBySubject(String subjectId) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(subjectId)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<GradeDto> getTeacherGradesBySubject(String subjectId, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(subjectId)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get().log()
                .build();

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<GradeDto> getStudentGradesBySubject(String subjectId, String studentUsername) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(subjectId)
                .path(GradeController.STUDENT)
                .path(GradeController.PATH_USERNAME).expand(studentUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<GradeDto> getGradesByEvaluation(String evaluationId) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(GradeController.GRADE)
                .path(GradeController.EVALUATION)
                .path(GradeController.PATH_ID).expand(evaluationId)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<GradeDto> getTeacherGradesByEvaluation(String evaluationId, String studentUsername) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(GradeController.GRADE)
                .path(GradeController.EVALUATION)
                .path(GradeController.PATH_ID).expand(evaluationId)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(studentUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();

    }

}
