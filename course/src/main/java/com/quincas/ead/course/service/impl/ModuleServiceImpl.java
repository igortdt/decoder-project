package com.quincas.ead.course.service.impl;

import com.quincas.ead.course.models.LessonModel;
import com.quincas.ead.course.models.ModuleModel;
import com.quincas.ead.course.repositories.LessonRepository;
import com.quincas.ead.course.repositories.ModuleRepository;
import com.quincas.ead.course.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {

        List<LessonModel> lessonModelList = lessonRepository.findAllLessonIntoModule(moduleModel.getModuleId());
        if(!lessonModelList.isEmpty()){
            lessonRepository.deleteAll(lessonModelList);
        }

        moduleRepository.delete(moduleModel);
    }

    @Override
    public ModuleModel save(ModuleModel moduleModel) {
        return moduleRepository.save(moduleModel);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findModuleIntoCourse(courseId, moduleId);
    }

    @Override
    public List<ModuleModel> findAll(UUID courseId) {
        return moduleRepository.findAllModulesIntoCourse(courseId);
    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {
        return moduleRepository.findById(moduleId);
    }

}

