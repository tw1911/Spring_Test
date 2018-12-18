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
public class OperationStageCrudServiceDBIntTest {

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
        OperationStage st1 = stageService.create(stage1);
        OperationStage fromDb = stageService.read(st1.getId());
        Assert.assertEquals(st1.getId(),fromDb.getId());
        Assert.assertEquals(st1.getOperationStageName(),fromDb.getOperationStageName());
        Assert.assertEquals(st1.getOperStageCode(),fromDb.getOperStageCode());
    }

    @Test
    public void createAllTest(){
        Assert.assertEquals(stageService.readAll().size(),0);
        stageService.createAll(stages);
        Assert.assertEquals(stageService.readAll().size(),stages.size());
    }

    @Test
    public void readTest(){
        stageService.createAll(stages);
        Assert.assertEquals(stageService.read(stages.get(0).getId()).getOperationStageName(),stage1.getOperationStageName());
    }

    @Test
    public void readAllTest(){
        Assert.assertEquals(stageService.createAll(stages).size(),stageService.readAll().size());
    }

    @Test
    public void updateTest(){
        stageService.create(stage4);
        stage4.setOperStageCode(OperationStageCode.CREATED);
        stageService.update(stage4);
        Assert.assertEquals(stage4.getOperStageCode(),stageService.read(stage4.getId()).getOperStageCode());
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExist(){
        stageService.update(stage4);
    }

    @Test
    public void deleteTest(){
        typeService.createAll(types);
        OperationStage fromDb = stageService.readAll().get(0);
        stageService.delete(fromDb);
        Assert.assertEquals(stageService.readAll().size(),stages.size()-1);
    }

    @Test
    public void deleteAllTest(){
        typeService.createAll(types);
        Assert.assertEquals(stageService.readAll().size(),stages.size());
        stageService.deleteAll();
        Assert.assertEquals(stageService.readAll().size(),0);
    }


    @After
    public void shutDown(){

    }
}
