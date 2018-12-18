package ru.tw1911.java.ee.test.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.repository.OperationTypeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class OperationTypeCrudService implements CrudService<OperationType>{

    OperationTypeRepository repository;

    @Autowired
    public OperationTypeCrudService(OperationTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public OperationType create(OperationType entity) {
        return repository.save(entity);
    }

    public List<OperationType> createAll(List<OperationType> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public OperationType update(OperationType entity) {
        if(entity.getId()!=null && repository.existsById(entity.getId())){
            return repository.save(entity);
        }
        else throw new EntityNotFoundException("Запись для изменения не найдена");
    }

    @Override
    public void delete(OperationType entity) {
        repository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
        else throw new EntityNotFoundException("Запись для удаления не найдена");;
    }

    @Override
    public void deleteAll(){
        repository.deleteAllInBatch();
    }

    @Override
    public void deleteAll(List<OperationType> types){
        repository.deleteInBatch(types);
    }

    @Override
    public OperationType read(Long id) {
        OperationType tmp = repository.getOne(id);
        Hibernate.initialize(tmp.getOperStages());
        return tmp;
    }

    @Override
    public List<OperationType> readAll() {
        return repository.findAll();
    }
}
