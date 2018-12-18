package ru.tw1911.java.ee.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationStageCode;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.entity.OperationTypeCode;
import ru.tw1911.java.ee.test.service.OperationStageCrudService;
import ru.tw1911.java.ee.test.service.OperationTypeCrudService;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class Main implements CommandLineRunner{

    @Autowired
    OperationTypeCrudService typeCrudService;
    @Autowired
    OperationStageCrudService stageCrudService;


    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
    public static LocalDateTime createLocalDateTime(){
        LocalDateTime result = LocalDateTime.now();
        int rnd = new Random().nextInt(14)-7;
        return result.plusDays(rnd);
    }

    public static List<OperationStage> createStageList(int size){
        ArrayList<OperationStage> stages= new ArrayList<>(size);
        for (int i=0;i<size;i++){
            OperationStageCode opStageCode = OperationStageCode.values()[i%OperationStageCode.values().length];
            stages.add(new OperationStage("Stage"+i,opStageCode));
        }
        return stages;
    }

    @Override
 //   @Transactional
    public void run(String... args) throws Exception {
        OperationType ot1 = new OperationType();
        ot1.setDateModified(createLocalDateTime());
        ot1.setOperationType(OperationTypeCode.CREATE);
        ot1.setOperTypeNumber(1);
        ot1.setOperationTypeName("Operation1");
        ot1.setOrderIndex(1);

        OperationType ot2 = new OperationType();
        ot2.setDateModified(createLocalDateTime());
        ot2.setOperationType(OperationTypeCode.UPDATE);
        ot2.setOperTypeNumber(2);
        ot2.setOperationTypeName("Operation2");
        ot2.setOrderIndex(10);

        OperationStage os1 = new OperationStage();
        os1.setOperationStageName("Stage 1");
        os1.setOperStageCode(OperationStageCode.CREATED);
        OperationStage os2 = new OperationStage();
        os2.setOperationStageName("Stage 2");
        os2.setOperStageCode(OperationStageCode.PROGRESS);
        OperationStage os3 = new OperationStage();
        os3.setOperationStageName("Stage 3");
        os3.setOperStageCode(OperationStageCode.DONE);
        OperationStage os4 = new OperationStage();
        os4.setOperationStageName("Stage 4");
        os4.setOperStageCode(OperationStageCode.ABORTED);

        List<OperationStage> stages= new ArrayList<>();
        Collections.addAll(stages,os1,os2,os3);
        List<OperationStage> stage2= new ArrayList<>();
        stage2.add(os4);
        List<OperationType> types = new ArrayList<>();
        Collections.addAll(types,ot1,ot2);
        types.forEach(type -> type.setOperStages(stages));
        stages.forEach(stage -> stage.setOperationTypes(types));
        //сохранение
//        stageCrudService.createAll(stages);
        typeCrudService.createAll(types);

    }

}
