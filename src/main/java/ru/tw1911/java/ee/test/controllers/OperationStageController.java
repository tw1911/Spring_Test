package ru.tw1911.java.ee.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tw1911.java.ee.test.dto.OperationStageDTO;
import ru.tw1911.java.ee.test.dto.OperationTypeDTO;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.service.OperationStageCrudService;
import ru.tw1911.java.ee.test.utils.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stages")
public class OperationStageController {
    private final OperationStageCrudService stageService;

    @Autowired
    private DtoMapper dtoMapper;

    @Autowired
    public OperationStageController(OperationStageCrudService stageService) {
        this.stageService = stageService;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    OperationStageDTO create(@RequestBody OperationStageDTO stageDTO){
        OperationStage stage = dtoMapper.map(stageDTO);
        return dtoMapper.map(stageService.create(stage));
    }

    @GetMapping
    List<OperationStageDTO> readAll(){
        List<OperationStage> stages = stageService.readAll();
        return stages.stream().map(stage -> dtoMapper.map(stage)).collect(Collectors.toList());
    }

    @GetMapping("/{stageId}")
    OperationStageDTO read(@PathVariable Long stageId){
        return dtoMapper.map(stageService.read(stageId));
    }


    @GetMapping("/{stageId}/types")
    List<OperationTypeDTO> readTypes(@PathVariable Long stageId){
        OperationStage stage = stageService.read(stageId);
        List<OperationType> types = stage.getOperationTypes();
        return types.stream().map(type->dtoMapper.map(type)).collect(Collectors.toList());
    }

    @PostMapping("/{stageId}")
    OperationStageDTO update(@PathVariable Long stageId, @RequestBody OperationStageDTO stageDTO){
        OperationStage stage = dtoMapper.map(stageDTO);
        stage.setOperationTypes(stageService.read(stage.getId()).getOperationTypes());
        return dtoMapper.map(stageService.update(stage));
    }

    @DeleteMapping("/{stageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long stageId){
        stageService.delete(stageId);
    }
}
