package com.ciberfisicos1.trazabilidad.repository.robot_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {
}
