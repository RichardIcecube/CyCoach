package coms309.Exercises;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Exercise findById(int id);

    @Transactional
    void deleteById(int id);

    @Query(value = "select * from exercise where exercise_name = ?1 limit 1", nativeQuery = true)
    Exercise findByName(String exerciseName);
}
