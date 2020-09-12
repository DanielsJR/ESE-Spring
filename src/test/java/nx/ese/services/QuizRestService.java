package nx.ese.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.QuizController;
import nx.ese.documents.core.CorrespondItem;
import nx.ese.documents.core.IncompleteTextItem;
import nx.ese.documents.core.MultipleSelectionItem;
import nx.ese.documents.core.QuizLevel;
import nx.ese.documents.core.SubjectName;
import nx.ese.documents.core.TrueFalseItem;
import nx.ese.dtos.QuizDto;

@Service
public class QuizRestService {

    @Autowired
    private RestService restService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private EvaluationRestService evaluationRestService;

    @Getter
    @Setter
    private QuizDto quizDto;

    @Getter
    @Setter
    private QuizDto quizDto2;

    @Getter
    private String teacherUsername;

    @Getter
    private String teacherUsername2;

    private static final Logger logger = LoggerFactory.getLogger(QuizRestService.class);

    public void createQuizesDto() {
        userRestService.createUsersDto();

        restService.loginManager();
        userRestService.postTeacher();
        userRestService.postTeacher2();

        teacherUsername = userRestService.getTeacherDto().getUsername();
        teacherUsername2 = userRestService.getTeacherDto2().getUsername();


        logger.info("*********************************CREATING_QUIZES*****************************************");
        this.quizDto = new QuizDto();
        this.quizDto.setTitle("quiz-test");
        this.quizDto.setDescription("this is a quiz test");
        this.quizDto.setAuthor(userRestService.getTeacherDto());
        this.quizDto.setSubjectName(SubjectName.MATEMATICAS);
        this.quizDto.setQuizLevel(QuizLevel.CUARTO_BASICO);
        this.quizDto.setShared(false);

        CorrespondItem ci = new CorrespondItem("fdf", "dsd");
        CorrespondItem ci2 = new CorrespondItem("gjg", "fdfd");
        List<CorrespondItem> ciL = new ArrayList<CorrespondItem>();
        ciL.add(ci);
        ciL.add(ci2);
        this.quizDto.setCorrespondItems(ciL);

        TrueFalseItem tfi = new TrueFalseItem("dad", true);
        TrueFalseItem tfi2 = new TrueFalseItem("gfgh", false);
        List<TrueFalseItem> tfiL = new ArrayList<TrueFalseItem>();
        tfiL.add(tfi);
        tfiL.add(tfi2);
        this.quizDto.setTrueFalseItems(tfiL);

        MultipleSelectionItem msi = new MultipleSelectionItem("re", "ds", "ss", "gd", "er", "ds");
        MultipleSelectionItem msi2 = new MultipleSelectionItem("sffs", "rf", "se", "wq", "sa", "sa");
        List<MultipleSelectionItem> msiL = new ArrayList<MultipleSelectionItem>();
        msiL.add(msi);
        msiL.add(msi2);
        this.quizDto.setMultipleSelectionItems(msiL);

        IncompleteTextItem iti = new IncompleteTextItem("ffs", "hrt");
        IncompleteTextItem iti2 = new IncompleteTextItem("fsd", "hhg");
        List<IncompleteTextItem> itiL = new ArrayList<IncompleteTextItem>();
        itiL.add(iti);
        itiL.add(iti2);
        this.quizDto.setIncompleteTextItems(itiL);

        //DTO2
        this.quizDto2 = new QuizDto();
        this.quizDto2.setTitle("quiz-test2");
        this.quizDto2.setDescription("this is a quiz test2");
        this.quizDto2.setAuthor(userRestService.getTeacherDto2());
        this.quizDto2.setSubjectName(SubjectName.INGLES);
        this.quizDto2.setQuizLevel(QuizLevel.CUARTO_BASICO);
        this.quizDto.setShared(false);

        CorrespondItem ci3 = new CorrespondItem("fdf", "dsd");
        CorrespondItem ci4 = new CorrespondItem("gjg", "fdfd");
        List<CorrespondItem> ciL2 = new ArrayList<CorrespondItem>();
        ciL2.add(ci3);
        ciL2.add(ci4);
        this.quizDto2.setCorrespondItems(ciL2);

        TrueFalseItem tfi3 = new TrueFalseItem("dad", true);
        TrueFalseItem tfi4 = new TrueFalseItem("gfgh", false);
        List<TrueFalseItem> tfiL2 = new ArrayList<TrueFalseItem>();
        tfiL2.add(tfi3);
        tfiL2.add(tfi4);
        this.quizDto2.setTrueFalseItems(tfiL2);

        MultipleSelectionItem msi3 = new MultipleSelectionItem("re", "ds", "ss", "gd", "er", "ds");
        MultipleSelectionItem msi4 = new MultipleSelectionItem("sffs", "rf", "se", "wq", "sa", "sa");
        List<MultipleSelectionItem> msiL2 = new ArrayList<MultipleSelectionItem>();
        msiL2.add(msi3);
        msiL2.add(msi4);
        this.quizDto2.setMultipleSelectionItems(msiL2);

        IncompleteTextItem iti3 = new IncompleteTextItem("ffs", "hrt");
        IncompleteTextItem iti4 = new IncompleteTextItem("fsd", "hhg");
        List<IncompleteTextItem> itiL2 = new ArrayList<IncompleteTextItem>();
        itiL2.add(iti3);
        itiL2.add(iti4);
        this.quizDto2.setIncompleteTextItems(itiL2);
        logger.info("**************************************************************************************");

    }

    public void deleteQuizesDto() {
        logger.info("*********************************DELETING_QUIZES**************************************");
        this.restService.loginUser(teacherUsername,teacherUsername+"@ESE1");
        try {
            this.deleteQuiz(this.getQuizDto().getId(), teacherUsername);
        } catch (Exception e) {
            logger.info("quizDto: nothing to delete");
        }
        this.restService.loginUser(teacherUsername2,teacherUsername2+"@ESE1");
        try {
            this.deleteQuiz(this.getQuizDto2().getId(), teacherUsername2);
        } catch (Exception e) {
            logger.info("quizDto2: nothing to delete");
        }

        this.userRestService.deleteTeachers();

        logger.info("********************************************************************************");

    }

    public QuizDto postQuiz(String teacherUsername) {
        return quizDto = restService.restBuilder(new RestBuilder<QuizDto>()).clazz(QuizDto.class)
                .path(QuizController.QUIZ)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(quizDto)
                .post()
                .build();
    }

    public QuizDto postQuiz2(String teacherUsername) {
        return quizDto2 = restService.restBuilder(new RestBuilder<QuizDto>()).clazz(QuizDto.class)
                .path(QuizController.QUIZ)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(quizDto2)
                .post()
                .build();

    }

    public QuizDto putQuiz(String teacherUsername) {
        return quizDto = restService.restBuilder(new RestBuilder<QuizDto>()).clazz(QuizDto.class)
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizDto.getId())
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(quizDto)
                .put()
                .build();
    }

    public QuizDto deleteQuiz(String id, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<QuizDto>()).clazz(QuizDto.class)
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(id)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    public QuizDto getQuizById(String quizId) {
        return restService.restBuilder(new RestBuilder<QuizDto>()).clazz(QuizDto.class)
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizId)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    public QuizDto getTeacherQuizById(String quizId, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<QuizDto>()).clazz(QuizDto.class)
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizId)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<QuizDto> getQuizes() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(QuizController.QUIZ)
                .bearerAuth(restService.getAuthToken().getToken())
                .get().log()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<QuizDto> getTeacherQuizes(String teacherUsername) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(QuizController.QUIZ)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<QuizDto> getQuizesByAuthor(String authorId) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(QuizController.QUIZ)
                .path(QuizController.AUTHOR)
                .path(QuizController.PATH_ID).expand(authorId)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<QuizDto> getTeacherQuizesByAuthor(String authorId, String teacherUsername) {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class)
                .path(QuizController.QUIZ)
                .path(QuizController.AUTHOR)
                .path(QuizController.PATH_ID).expand(authorId)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

}
