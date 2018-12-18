package ru.tw1911.java.ee.test.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.repository.OperationStageRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class OperationStageCrudService  implements CrudService<OperationStage>{

    private OperationStageRepository repository;

    @Override
    public OperationStage create(OperationStage entity) {
        return repository.save(entity);
    }

    public List<OperationStage> createAll(List<OperationStage> entities) {
        return repository.saveAll(entities);
    }


    @Override
    public OperationStage update(OperationStage entity) {
        if(entity.getId()!=null && repository.existsById(entity.getId())){
            return repository.save(entity);
        }
        else throw new EntityNotFoundException("Запись для изменения не найдена");
    }

    @Override
    public void delete(OperationStage entity) {
        repository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) repository.deleteById(id);
        else throw new EntityNotFoundException("Операция не найдена");
    }

    @Override
    public void deleteAll() {
        repository.deleteAllInBatch();
    }

    @Override
    public void deleteAll(List<OperationStage> entities) {
        repository.deleteInBatch(entities);
    }

    @Autowired
    public OperationStageCrudService(OperationStageRepository stageRepository) {
        this.repository = stageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OperationStage read(Long id) {
        OperationStage tmp = repository.getOne(id);
        Hibernate.initialize(tmp);
        return tmp;
    }

    @Override
    public List<OperationStage> readAll() {
        return repository.findAll();
    }
}
