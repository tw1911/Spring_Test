package ru.tw1911.java.ee.test.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import ru.tw1911.java.ee.test.dto.OperationStageDTO;
import ru.tw1911.java.ee.test.dto.OperationTypeDTO;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationType;

public class DtoMapper {

    ModelMapper typeToDTO;
    ModelMapper stageToDTO;
    ModelMapper DTOtoType;
    ModelMapper DTOtoStage;

    public OperationStage map(OperationStageDTO dto){
        return DTOtoStage.map(dto,OperationStage.class);
    }
    public OperationType map(OperationTypeDTO dto){
        return DTOtoType.map(dto,OperationType.class);
    }
    public OperationStageDTO map(OperationStage stage){
        return stageToDTO.map(stage,OperationStageDTO.class);
    }
    public OperationTypeDTO map(OperationType type){
        return typeToDTO.map(type,OperationTypeDTO.class);
    }

    public DtoMapper(){
        typeToDTO = new ModelMapper();
        typeToDTO.addMappings(new PropertyMap<OperationType, OperationTypeDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
            }
        });
        stageToDTO  = new ModelMapper();
        stageToDTO.addMappings(new PropertyMap<OperationStage, OperationStageDTO>() {
            @Override
            protected void configure(){
                map().setId(source.getId());
            }
        });
        DTOtoType = new ModelMapper();
        DTOtoType.addMappings(new PropertyMap<OperationTypeDTO, OperationType>() {
            @Override
            protected void configure(){
                map().setId(source.getId());
            }
        });
        DTOtoStage = new ModelMapper();
        DTOtoStage.addMappings(new PropertyMap<OperationStageDTO, OperationStage>() {
            @Override
            protected void configure(){
                map().setId(source.getId());
            }
        });
    }
}
