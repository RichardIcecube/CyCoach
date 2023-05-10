package coms309.Managers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findById(int id);
    @Transactional
    void deleteById(int id);
}
