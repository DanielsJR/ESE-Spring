package nx.ese.repositories;


import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Attendance;
import nx.ese.dtos.AttendanceDto;


public interface AttendanceRepository extends MongoRepository<Attendance, String> {
	
	public AttendanceDto  findFirstBySubjectId(String subjectId);
	
	public AttendanceDto  findFirstByDate(String quizId);

	public AttendanceDto findBySubjectIdAndDate(String subjectId, Date date);
	
	public List<AttendanceDto> findBySubjectId(String subjectId);
	

}
