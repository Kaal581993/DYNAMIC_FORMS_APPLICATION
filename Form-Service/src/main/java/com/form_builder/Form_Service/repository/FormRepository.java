package com.form_builder.Form_Service.repository;

import com.form_builder.Form_Service.model.Form;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormRepository extends MongoRepository<Form, String> {

    Optional<Form> findByName(String name);

}
