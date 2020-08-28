package nx.ese.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nx.ese.dtos.validators.NxPattern;
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

    public static String EVALUATION_TITLE_01 = "EvaluationTest01";
    public static String EVALUATION_TITLE_02 = "EvaluationTest02";


    private static final Logger logger = LoggerFactory.getLogger(GradeRestService.class);

    public void createEvaluationsDto() {
        subjectRestService.createSubjectsDto();

        restService.loginManager();
        subjectRestService.postSubject();

        logger.info("*****************************CREATING_EVALUATIONS********************************************");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NxPattern.DATE_FORMAT);

        this.evaluationDto = new EvaluationDto();
        this.evaluationDto.setType(EvaluationType.DISERTACION);
        this.evaluationDto.setTitle(EVALUATION_TITLE_01);
        this.evaluationDto.setDate(LocalDate.of(2018, 01, 11));
        this.evaluationDto.setSubject(subjectRestService.getSubjectDto());

        this.evaluationDto2 = new EvaluationDto();
        this.evaluationDto2.setType(EvaluationType.PRUEBA);
        this.evaluationDto2.setTitle(EVALUATION_TITLE_02);
        this.evaluationDto2.setDate(LocalDate.of(2018, 03, 05));
        this.evaluationDto2.setSubject(subjectRestService.getSubjectDto());

        logger.info("*****************************************************************************************");
    }

    public void deleteEvaluationsDto() {
        logger.info("*****************************DELETING_EVALUATIONS********************************************");

        this.restService.loginTeacher();

        try {
            this.deleteEvaluation(this.getEvaluationDto().getId());
        } catch (Exception e) {
            logger.info("evaluationDto: nothing to delete");
        }

        try {
            this.deleteEvaluation(this.getEvaluationDto2().getId());
        } catch (Exception e) {
            logger.info("evaluationDto2: nothing to delete");
        }

        this.subjectRestService.deleteSubjects();

        logger.info("***************************************************************************************");

    }

    public void postEvaluation() {
        evaluationDto = restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(evaluationDto)
                .post()
                .build();
    }

    public void postEvaluation2() {
        evaluationDto2 = restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(evaluationDto2)
                .post()
                .build();
    }

    public void putEvaluation() {
        evaluationDto = restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(evaluationDto.getId())
                .bearerAuth(restService.getAuthToken().getToken())
                .body(evaluationDto)
                .put()
                .build();
    }

    public void deleteEvaluation(String id) {
        restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }


    public EvaluationDto getEvaluationById(String id) {
        return restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    public EvaluationDto getTeacherEvaluationById(String evaluationtId, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(evaluationtId)
                .path(EvaluationController.TEACHER)
                .path(EvaluationController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<EvaluationDto> getEvaluationsBySubject(String subjectId) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(subjectId)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<EvaluationDto> getTeacherEvaluationsBySubject(String subjectId, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(subjectId)
                .path(EvaluationController.TEACHER)
                .path(EvaluationController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    public EvaluationDto getEvaluationBySubjectAndDate(String subjectId, LocalDate date) {
        return restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(subjectId)
                .path(EvaluationController.DATE)
                .path(EvaluationController.PATH_DATE).expand(date)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    public EvaluationDto getTeacherEvaluationBySubjectAndDate(String subjectId, LocalDate date,String teacherUsername) {
        return restService.restBuilder(new RestBuilder<EvaluationDto>()).clazz(EvaluationDto.class)
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(subjectId)
                .path(EvaluationController.DATE)
                .path(EvaluationController.PATH_DATE).expand(date)
                .path(EvaluationController.TEACHER)
                .path(EvaluationController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }


}
