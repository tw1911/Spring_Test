package ru.tw1911.java.ee.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tw1911.java.ee.test.entity.OperationType;

public interface OperationTypeRepository extends JpaRepository<OperationType,Long> {
}
