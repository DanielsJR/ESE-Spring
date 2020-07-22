package nx.ese.repositories;

import nx.ese.documents.core.Course;

public interface CourseRepositoryCustom {
	
	public Course findByChiefTeacherCustom(String name);

}
