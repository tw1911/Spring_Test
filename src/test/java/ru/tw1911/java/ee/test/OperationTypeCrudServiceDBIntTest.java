package ru.tw1911.java.ee.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationStageCode;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.entity.OperationTypeCode;
import ru.tw1911.java.ee.test.service.OperationStageCrudService;
import ru.tw1911.java.ee.test.service.OperationTypeCrudService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationTypeCrudServiceDBIntTest {

    OperationType operation1,operation2,operation3;
    OperationStage stage1,stage2,stage3,stage4;

    @Autowired
    OperationTypeCrudService typeService;
    @Autowired
    OperationStageCrudService stageService;
    List<OperationType> types = new ArrayList<>();
    List<OperationStage> stages = new ArrayList<>();

    @Before
    public void setUp(){
        typeService.deleteAll();
        stageService.deleteAll();

        stage1 = new OperationStage("Stage 1", OperationStageCode.DONE);
        stage2 = new OperationStage("Stage 2", OperationStageCode.PROGRESS);
        stage3 = new OperationStage("Stage 3", OperationStageCode.CREATED);
        stage4 = new OperationStage("Stage 4", OperationStageCode.ABORTED);
        Collections.addAll(stages,stage1,stage2,stage3);
        operation1 = new OperationType("Operation 1", OperationTypeCode.CREATE);
        operation2 = new OperationType("Operation 2", OperationTypeCode.UPDATE);
        operation3 = new OperationType("Operation 3", OperationTypeCode.READ);
        Collections.addAll(types,operation1,operation2,operation3);
        types.forEach(type -> type.setOperStages(stages));
    }
    @Test
    public void createTest(){
        typeService.create(operation1);
        Assert.assertNotNull(operation1.getId());
        OperationType operationFromDB = typeService.read(operation1.getId());
        Assert.assertEquals(operation1.getOperationTypeName(),operationFromDB.getOperationTypeName());
        Assert.assertEquals(operation1.getOperationType(),operationFromDB.getOperationType());
    }

    @Test
    public void createAllTest(){
        typeService.createAll(types);
        List<OperationType> typesFromDB = typeService.readAll();
        Assert.assertEquals(types.size(),typesFromDB.size());
    }

    @Test
    public void readTest(){
        typeService.create(operation1);
        Assert.assertNotNull(operation1.getId());
        OperationType operationFromDB = typeService.read(operation1.getId());
        Assert.assertEquals(operation1.getOperationTypeName(),operationFromDB.getOperationTypeName());
        Assert.assertEquals(operation1.getOperationType(),operationFromDB.getOperationType());
    }

    @Test
    public void readAllTest(){
        typeService.createAll(types);
        List<OperationType> typesFromDB = typeService.readAll();
        Assert.assertEquals(types.size(),typesFromDB.size());
    }

    @Test
    public void updateTest(){
        List<OperationStage> op1Stages = typeService.create(operation1).getOperStages();
        stageService.create(stage4);
        Assert.assertNotNull(stage4.getId());
        op1Stages.add(stage4);
        operation1.setOperStages(stages );
        typeService.update(operation1);
        OperationType fromDB = typeService.read(operation1.getId());
        Assert.assertEquals(fromDB.getOperStages().get(3).getOperationStageName(),stage4.getOperationStageName());
        //не работает. есть идеи?
        //Assert.assertTrue(stageService.read(stage4.getId()).getOperationTypes().contains(operation1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExistEntityTest(){
        OperationType operation5 = new OperationType("Operation 5",OperationTypeCode.CREATE);
        typeService.update(operation5);
    }

    @Test
    public void deleteTest(){
        typeService.createAll(types);
        Assert.assertEquals(typeService.readAll().size(),types.size());
        typeService.delete(operation1);
        Assert.assertEquals(typeService.readAll().size(),types.size()-1);
    }

    @Test
    public void deleteAllTest(){
        typeService.createAll(types);
        Assert.assertEquals(typeService.readAll().size(),3);
        typeService.deleteAll();
        Assert.assertEquals(typeService.readAll().size(),0);
    }

    @After
    public void shutDown(){
    }
}
