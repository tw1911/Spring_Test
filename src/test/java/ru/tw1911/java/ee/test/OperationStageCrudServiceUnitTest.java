package ru.tw1911.java.ee.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationStageCode;
import ru.tw1911.java.ee.test.repository.OperationStageRepository;
import ru.tw1911.java.ee.test.service.OperationStageCrudService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OperationStageCrudServiceUnitTest {
    @Mock
    OperationStageRepository repository;

    OperationStageCrudService service;

    @Before
    public void setUp() {
        this.repository = mock(OperationStageRepository.class);
        service = new OperationStageCrudService(repository);
    }

    @Test
    public void createTest(){
        OperationStage st1 = new OperationStage("Stage_1", OperationStageCode.PROGRESS);
        when(repository.save(st1)).thenReturn(st1);
        Assert.assertNotNull(service.create(st1));
    }

    @Test
    public void readTest(){
        when(repository.getOne(anyLong())).thenReturn(new OperationStage("Stage_1", OperationStageCode.PROGRESS));
        Assert.assertEquals(service.read(1L).getOperationStageName(),"Stage_1");
    }

    @Test
    public void readAllTest(){
        List<OperationStage> stages = new ArrayList<>(Arrays.asList(
                new OperationStage("Stage_1", OperationStageCode.PROGRESS),
                new OperationStage("Stage_2", OperationStageCode.DONE)));
        when(repository.findAll()).thenReturn(stages);
        Assert.assertEquals(service.readAll().size(), 2);
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    public void updateTest(){
        OperationStage st1 = new OperationStage("Stage_1", OperationStageCode.PROGRESS);
        st1.setId(1l);
        when(repository.existsById(st1.getId())).thenReturn(true);
        when(repository.save(st1)).thenReturn(st1);
        Assert.assertNotNull(service.update(st1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExistEntityTest(){
        OperationStage st1 = new OperationStage("Stage_1", OperationStageCode.PROGRESS);
        st1.setId(1l);
        when(repository.existsById(st1.getId())).thenReturn(false);
        service.update(st1);
    }

    @Test
    public void deleteTest(){
        OperationStage st1 = new OperationStage("Stage_1", OperationStageCode.PROGRESS);
        st1.setId(1l);
        service.delete(st1);
        verify(repository, atLeastOnce()).delete(st1);
    }

    @Test
    public void deleteByIdTest(){
        OperationStage st1 = new OperationStage("Stage_1", OperationStageCode.PROGRESS);
        st1.setId(1l);
        when(repository.existsById(st1.getId())).thenReturn(true);
        service.delete(st1.getId());
        verify(repository, atLeastOnce()).deleteById(st1.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteNonExistEntityTest(){
        when(repository.existsById(4l)).thenReturn(false);
        service.delete(4l);
    }
}
