package ru.tw1911.java.ee.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tw1911.java.ee.test.dto.OperationStageDTO;
import ru.tw1911.java.ee.test.dto.OperationTypeDTO;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.service.OperationStageCrudService;
import ru.tw1911.java.ee.test.service.OperationTypeCrudService;
import ru.tw1911.java.ee.test.utils.DtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operations")
public class OperationTypeController {

    private final OperationTypeCrudService typeService;
    private final OperationStageCrudService stageService;

    @Autowired
    private DtoMapper dtoMapper;

    @Autowired
    public OperationTypeController(OperationTypeCrudService typeService, OperationStageCrudService stageService) {
        this.typeService = typeService;
        this.stageService = stageService;
    }

    @PutMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public OperationTypeDTO create(@RequestBody OperationTypeDTO typeDTO){
        OperationType type = dtoMapper.map(typeDTO);
        type = typeService.create(type);
        return dtoMapper.map(type);
    }

    @GetMapping
    List<OperationTypeDTO> read(){
        System.out.println("I'm get service!");
        List<OperationTypeDTO> typeDTOList = new ArrayList<>();
        for (OperationType type: typeService.readAll()){
            typeDTOList.add(dtoMapper.map(type));
        }
        return typeDTOList;
    }


    @GetMapping(value = "/{operationId}")
    OperationTypeDTO read(@PathVariable Long operationId) {
        OperationType operationType = typeService.read(operationId);
        return dtoMapper.map(operationType);
    }

    @GetMapping(value = "/{operationId}/stages")
    List<OperationStageDTO> readStages(@PathVariable Long operationId){
        List<OperationStage> stages = typeService.read(operationId).getOperStages();
        return stages.stream().map(stage -> dtoMapper.map(stage)).collect(Collectors.toList());
    }

    @PostMapping(value = "/{operationId}/stages")
    @ResponseBody
    List<OperationStageDTO> updateStages(@PathVariable Long operationId, @RequestBody List<OperationStageDTO> stagesDTO){
        List<OperationStage> stages = stagesDTO.stream().map(operationStageDTO -> stageService.read(operationStageDTO.getId())).collect(Collectors.toList());
        OperationType type = typeService.read(operationId);
        type.setOperStages(stages);
        stages = typeService.update(type).getOperStages();
        return stages.stream().map(stage -> dtoMapper.map(stage)).collect(Collectors.toList());
    }

    @PostMapping(value = "/{operationId}")
    @ResponseBody
    OperationTypeDTO update(@RequestBody OperationTypeDTO typeDTO){
        OperationType type = dtoMapper.map(typeDTO);
        type.setOperStages(typeService.read(type.getId()).getOperStages());
        return dtoMapper.map(typeService.update(type));
    }



    @DeleteMapping(value = "/{operationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long operationId) {
        typeService.delete(operationId);
    }

}
