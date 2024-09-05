package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.food.model.Events;

import java.util.List;

public interface EventRepository extends JpaRepository<Events, Long> {
    public List<Events> findEventsByRestaurantId(Long id);
}
