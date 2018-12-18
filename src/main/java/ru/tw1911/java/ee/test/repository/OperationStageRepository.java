package ru.tw1911.java.ee.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tw1911.java.ee.test.entity.OperationStage;

public interface OperationStageRepository  extends JpaRepository<OperationStage,Long>{

}
