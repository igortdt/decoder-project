package com.quincas.ead.course.service.impl;

import com.quincas.ead.course.models.CourseModel;
import com.quincas.ead.course.models.LessonModel;
import com.quincas.ead.course.models.ModuleModel;
import com.quincas.ead.course.repositories.CourseRepository;
import com.quincas.ead.course.repositories.LessonRepository;
import com.quincas.ead.course.repositories.ModuleRepository;
import com.quincas.ead.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if(!moduleModelList.isEmpty()){
            moduleModelList.forEach(moduleModel -> {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonIntoModule(moduleModel.getModuleId());
                if(!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList);
                }
            });

            moduleRepository.deleteAll(moduleModelList);
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID userId) {
        return courseRepository.findById(userId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
