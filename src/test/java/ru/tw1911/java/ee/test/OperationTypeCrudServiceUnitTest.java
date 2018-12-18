package ru.tw1911.java.ee.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.entity.OperationTypeCode;
import ru.tw1911.java.ee.test.repository.OperationTypeRepository;
import ru.tw1911.java.ee.test.service.OperationTypeCrudService;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OperationTypeCrudServiceUnitTest {

    @Mock
    OperationTypeRepository repository;

    OperationTypeCrudService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.repository = mock(OperationTypeRepository.class);
        this.service = new OperationTypeCrudService(this.repository);
    }


    @Test
    public void createTest(){
        OperationType type1 = new OperationType("Operation_1", OperationTypeCode.CREATE);
        OperationType type2 = new OperationType("Operation_2", OperationTypeCode.CREATE);
        when(repository.save(type1)).thenReturn(type2);
        Assert.assertEquals(service.create(type1).getOperationTypeName(),type2.getOperationTypeName());
    }

    @Test
    public void updateTest(){
        OperationType type1 = new OperationType("Operation_1", OperationTypeCode.CREATE);
        type1.setId(1l);
        when(repository.existsById(type1.getId())).thenReturn(true);
        when(repository.save(type1)).thenReturn(type1);
        Assert.assertNotNull(service.update(type1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExistEntityTest(){
        OperationType type1 = new OperationType("Operation_1", OperationTypeCode.CREATE);
        type1.setId(1l);
        when(repository.existsById(type1.getId())).thenReturn(false);
        service.update(type1);
    }

    @Test
    public void deleteTest(){
        OperationType type1 = new OperationType("Operation_1", OperationTypeCode.CREATE);
        type1.setId(1l);
        service.delete(type1);
        verify(repository, atLeastOnce()).delete(type1);
    }

    @Test
    public void deleteByIdTest(){
        OperationType type1 = new OperationType("Operation_1", OperationTypeCode.CREATE);
        type1.setId(1l);
        when(repository.existsById(type1.getId())).thenReturn(true);
        service.delete(type1.getId());
        verify(repository, atLeastOnce()).deleteById(type1.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteNonExistEntityTest(){
        when(repository.existsById(4l)).thenReturn(false);
        service.delete(4l);
    }
}
