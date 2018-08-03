package nx.ESE.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.core.Course;
import nx.ESE.dtos.CourseDto;
import nx.ESE.repositories.CourseRepository;

@Controller
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	private void setCourseFromDto(Course course, CourseDto courseDto) {
		course.setName(courseDto.getName());
		course.setChiefTeacher(courseDto.getChiefTeacher());
		course.setStudents(courseDto.getStudents());
		course.setPeriod(courseDto.getPeriod());
	}

	// CRUD******************************
	public Optional<CourseDto> getCourseById(String id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}

	public List<CourseDto> getFullCourses() {
		// Date date = new Date();
		// date.setYear(2017);
		List<CourseDto> coursesList = new ArrayList<>();

		for (Course course : courseRepository.findAll()) {
			coursesList.add(new CourseDto(course.getId(), course.getName(), course.getChiefTeacher(),
					course.getStudents(), course.getPeriod()));
		}

		return coursesList;
	}

	public CourseDto createCourse(CourseDto courseDto) {
		Course course = new Course();
		this.setCourseFromDto(course, courseDto);
		courseRepository.save(course);
		return new CourseDto(courseRepository.findById(course.getId()).get());
	}

	public Optional<CourseDto> modifyCourse(String id, CourseDto courseDto) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			this.setCourseFromDto(course.get(), courseDto);
			courseRepository.save(course.get());
			return Optional.of(new CourseDto(courseRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<CourseDto> deleteCourse(String id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			courseRepository.deleteById(id);
			return Optional.of(new CourseDto(course.get()));
		}
		return Optional.empty();

	}

}
