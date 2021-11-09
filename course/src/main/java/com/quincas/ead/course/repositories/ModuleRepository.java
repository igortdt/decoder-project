package com.quincas.ead.course.repositories;

import com.quincas.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

     //Utlizado para buscar listas mesmo quando anotada com LAZY
//    @EntityGraph(attributePaths = {"course"})
//    ModuleModel findByTitle(String title);

//    //Utilizado para alterar um dado de forma manual, usando o comando update ou delete
//    @Modifying


    @Query(value = "select * from tb_modules where course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "select * from tb_modules where course_course_id = :courseId and module_id = :moduleId ", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId")  UUID moduleId);
}
