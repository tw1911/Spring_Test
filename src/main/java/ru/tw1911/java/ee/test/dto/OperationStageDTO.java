package ru.tw1911.java.ee.test.dto;

import ru.tw1911.java.ee.test.entity.OperationStageCode;

import java.time.LocalDateTime;

public class OperationStageDTO{

    private long id;

    OperationStageCode operStageCode;

    String operationStageName;

    private LocalDateTime dateModified;

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OperationStageCode getOperStageCode() {
        return operStageCode;
    }

    public void setOperStageCode(OperationStageCode operStageCode) {
        this.operStageCode = operStageCode;
    }

    public String getOperationStageName() {
        return operationStageName;
    }

    public void setOperationStageName(String operationStageName) {
        this.operationStageName = operationStageName;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

}
