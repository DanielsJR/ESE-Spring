package nx.ese.repositories;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import nx.ese.documents.core.Course;

public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {
	
	@Autowired
    private MongoTemplate mongoTemplate;

	
	@Override
	public Course findByChiefTeacherCustom(String id) {
		Query query = new Query();
		   query.addCriteria(Criteria.where("chiefTeacher.$id").is(new ObjectId(id)));
		   return mongoTemplate.findOne(query, Course.class);
	}

}
