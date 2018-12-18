package ru.tw1911.java.ee.test.service;

import ru.tw1911.java.ee.test.entity.AbstractModel;

import java.util.List;

public interface CrudService<T extends AbstractModel> {
    T create(T entity);
    T update(T entity);
    void delete(T entity);
    void delete(Long id);
    void deleteAll();
    void deleteAll(List<T> entities);
    T read(Long id);
    List<T> readAll();
}
