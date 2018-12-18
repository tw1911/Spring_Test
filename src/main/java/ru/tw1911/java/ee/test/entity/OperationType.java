package ru.tw1911.java.ee.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "OPERATION_TYPE_ID"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OperationType extends AbstractModel implements Comparable<OperationType>{

    private static final String OPER_TYPE_STR = "Тип операции";
    private static final String ORDER_INDEX_STR = "Порядок сортировки";

    @Column(name = "OPERATION_TYPE_CODE",length = 50)
    private OperationTypeCode operationType;

    @Column(name = "OPERATION_TYPE_NAME", length = 50)
    private String operationTypeName;

    @Column(name="ORDER_INDEX")
    private Integer orderIndex;

    @Column(name="DATE_MODIFIED")
    private LocalDateTime dateModified;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "OPER_TYPE_OPER_STAGE",
            joinColumns = { @JoinColumn(name = "OPERATION_TYPE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "OPERATION_STAGE_ID") })
    @Fetch(FetchMode.SUBSELECT)
    private List<OperationStage> operStages;

    private transient Integer operTypeNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OperationType that = (OperationType) o;

        if (operationType != that.operationType) return false;
        if (operationTypeName != null ? !operationTypeName.equals(that.operationTypeName) : that.operationTypeName != null)
            return false;
        if (orderIndex != null ? !orderIndex.equals(that.orderIndex) : that.orderIndex != null) return false;
        if (dateModified != null ? !dateModified.equals(that.dateModified) : that.dateModified != null) return false;
        if (operStages != null ? !operStages.equals(that.operStages) : that.operStages != null) return false;
        return operTypeNumber != null ? operTypeNumber.equals(that.operTypeNumber) : that.operTypeNumber == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
        result = 31 * result + (operationTypeName != null ? operationTypeName.hashCode() : 0);
        result = 31 * result + (orderIndex != null ? orderIndex.hashCode() : 0);
        result = 31 * result + (dateModified != null ? dateModified.hashCode() : 0);
        result = 31 * result + (operStages != null ? operStages.hashCode() : 0);
        result = 31 * result + (operTypeNumber != null ? operTypeNumber.hashCode() : 0);
        return result;
    }

    public OperationType(String operationTypeName, Integer orderIndex, OperationTypeCode operTypeCode, Integer operTypeNumber) {
        this.operationTypeName = operationTypeName;
        this.orderIndex = orderIndex;
        this.operationType = operTypeCode;
        this.operTypeNumber=operTypeNumber;
        operStages = new ArrayList<OperationStage>();
    }


    public OperationType(String operationTypeName, Integer orderIndex) {
        this(operationTypeName, orderIndex, null,null);
    }

    public OperationType(String operationTypeName, Integer orderIndex, OperationTypeCode operationType) {
        this.operationTypeName = operationTypeName;
        this.orderIndex = orderIndex;
        this.operationType = operationType;
    }

    public OperationType(String operationTypeName, OperationTypeCode operTypeCode, Integer operTypeNumber) {
        this(operationTypeName, null, operTypeCode,operTypeNumber);
    }

    public OperationType(String operationTypeName, OperationTypeCode operTypeCode) {
        this(operationTypeName, operTypeCode,null);
    }


    public OperationType(){}

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

    public OperationTypeCode getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeCode operationType) {
        this.operationType = operationType;
    }

    public Integer getOperTypeNumber() {
        return operTypeNumber;
    }

    public void setOperTypeNumber(Integer operTypeNumber) {
        this.operTypeNumber = operTypeNumber;
    }

    public List<OperationStage> getOperStages() {
        return operStages;
    }

    public void setOperStages(List<OperationStage> operStages) {
        this.operStages = operStages;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public void getInfo() {
        System.out.println(getPrintString());
    }

    public String getPrintString() {
        String ddot = ": ";
        String cdot ="; ";
        StringBuilder result = new StringBuilder();
        result.append("Id").append(ddot).append(getId()).append(cdot)
                .append("DateTime").append(ddot).append(dateModified).append(cdot)
                .append(ORDER_INDEX_STR).append(ddot).append(orderIndex).append(cdot)
                .append(OPER_TYPE_STR).append(ddot).append(operationTypeName).append(cdot)
                .append("OperationTypeCode").append(ddot).append(operationType).append(cdot)
                .append("OperTypeNumber").append(ddot).append(operTypeNumber).append(cdot)
                .append("operStages").append(ddot).append("{");
                if (operStages!=null)
                    operStages.forEach(os -> result.append(os.getPrintString()).append(","));
        result.append("}");
        return result.toString();
    }

    public String toString(){
        return getPrintString();
    }

    public void addOrderIndex(int addIndex, boolean isIf) {
        int rem = addIndex % 3;
        if (isIf) {
            if (rem == 0) {
                orderIndex += addIndex;
            } else if (rem == 1) {
                orderIndex -= addIndex;
            } else orderIndex *= Math.abs(addIndex);
        } else {
            switch (rem) {
                case 0:
                    orderIndex += addIndex;
                    break;
                case 1:
                    orderIndex -= addIndex;
                    break;
                case 2:
                    orderIndex *= Math.abs(addIndex);
            }
        }
    }
    public int compareTo(OperationType o) {
        int result = orderIndex.compareTo(o.getOrderIndex());
        if(result==0){
            result=operationTypeName.compareTo(o.getOperationTypeName());
        }
        return result;
    }

    public void removeStage(OperationStage stage) {
        this.operStages.remove(stage);
    }
}
