package ru.tw1911.java.ee.test.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "OPERATION_STAGE_ID"))
public class OperationStage extends AbstractModel{
    @Column(name = "OPERATION_STAGE_CODE")
    OperationStageCode operStageCode;

    @Column(name = "OPERATION_STAGE_NAME")
    String operationStageName;

    @Column(name="DATE_MODIFIED")
    private LocalDateTime dateModified;

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OperationStage stage = (OperationStage) o;

        if (operStageCode != stage.operStageCode) return false;
        if (operationStageName != null ? !operationStageName.equals(stage.operationStageName) : stage.operationStageName != null)
            return false;
        if (dateModified != null ? !dateModified.equals(stage.dateModified) : stage.dateModified != null) return false;
        return operationTypes != null ? operationTypes.equals(stage.operationTypes) : stage.operationTypes == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (operStageCode != null ? operStageCode.hashCode() : 0);
        result = 31 * result + (operationStageName != null ? operationStageName.hashCode() : 0);
        result = 31 * result + (dateModified != null ? dateModified.hashCode() : 0);
        result = 31 * result + (operationTypes != null ? operationTypes.hashCode() : 0);
        return result;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public List<OperationType> getOperationTypes() {
        return operationTypes;
    }

    public void setOperationTypes(List<OperationType> operationTypes) {
        this.operationTypes = operationTypes;
    }

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "operStages")
    private List<OperationType> operationTypes;

    public OperationStage(String operationStageName, OperationStageCode operStageCode) {
        this.operationStageName = operationStageName;
        this.operStageCode = operStageCode;
    }

    public OperationStage(String operationStageName) {
        this.operationStageName = operationStageName;
    }

    public OperationStage(OperationStageCode operStageCode) {
        this.operStageCode = operStageCode;
    }

    public OperationStage(Long id) {
        super(id);
    }

    public OperationStage(){};

    public String getOperationStageName() {
        return operationStageName;
    }

    public void setOperationStageName(String operationStageName) {
        this.operationStageName = operationStageName;
    }

    public OperationStageCode getOperStageCode() {
        return operStageCode;
    }

    public void setOperStageCode(OperationStageCode operStageCode) {
        this.operStageCode = operStageCode;
    }

    @Override
    public void getInfo() {
        System.out.println(getPrintString());
    }

    public String getPrintString(){
        return new StringBuffer().append(operationStageName).append(operStageCode).toString();
    }

    @PreRemove
    private void removeStageFromOperation(){
        for (OperationType type: operationTypes) type.removeStage(this);
    }

    private void addOperationStage(){}

    @Override
    public String toString() {
        return getPrintString();
    }
}
