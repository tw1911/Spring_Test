package ru.tw1911.java.ee.test.dto;

import ru.tw1911.java.ee.test.entity.OperationTypeCode;

import java.time.LocalDateTime;

public class OperationTypeDTO {

    private long id;

    private OperationTypeCode operationType;

    private String operationTypeName;

    private Integer orderIndex;

    private LocalDateTime dateModified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OperationTypeCode getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeCode operationType) {
        this.operationType = operationType;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

}
