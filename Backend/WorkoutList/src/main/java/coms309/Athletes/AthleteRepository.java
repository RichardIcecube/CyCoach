package coms309.Athletes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    Athlete findById(int id);

    @Transactional
    void deleteById(int id);
}
