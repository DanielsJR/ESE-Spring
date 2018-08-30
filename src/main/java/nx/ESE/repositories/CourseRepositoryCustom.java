package nx.ESE.repositories;

import java.util.Optional;

import nx.ESE.documents.core.Course;

public interface CourseRepositoryCustom {
	
	public Course findByChiefTeacherCustom(String name);

}
