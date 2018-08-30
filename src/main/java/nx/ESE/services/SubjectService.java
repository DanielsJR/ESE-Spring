package nx.ESE.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.core.Subject;
import nx.ESE.dtos.SubjectDto;
import nx.ESE.repositories.SubjectRepository;

@Controller
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	private void setSubjectFromDto(Subject subject, SubjectDto subjectDto) {
		subject.setName(subjectDto.getName());
		subject.setTeacher(subjectDto.getTeacher());
		subject.setCourse(subjectDto.getCourse());
	}

	public boolean existsById(String id) {
		return subjectRepository.existsById(id);
	}

	// CRUD******************************
	public List<SubjectDto> getFullSubjects() {
		List<SubjectDto> listSubjects = new ArrayList<>();

		for (Subject subject : subjectRepository.findAll()) {
			listSubjects
					.add(new SubjectDto(subject.getId(), subject.getName(), subject.getTeacher(), subject.getCourse()));
		}
		return listSubjects;
	}

	public SubjectDto getSubjectById(String id) {
		Subject subject = subjectRepository.findById(id).get();
		return new SubjectDto(subject);
	}

	public SubjectDto createSubject(@Valid SubjectDto subjectDto) {
		Subject subject = new Subject();
		setSubjectFromDto(subject, subjectDto);
		subjectRepository.insert(subject);
		return new SubjectDto(subjectRepository.findById(subject.getId()).get());
	}

	public SubjectDto modifySubject(String id, @Valid SubjectDto subjectDto) {
		Subject subject = subjectRepository.findById(id).get();
		setSubjectFromDto(subject, subjectDto);
		subjectRepository.save(subject);
		return new SubjectDto(subjectRepository.findById(subject.getId()).get());
	}

	public boolean deleteSubject(String id) {
		Subject subject = subjectRepository.findById(id).get();
		subjectRepository.delete(subject);
		return true;
	}

}
