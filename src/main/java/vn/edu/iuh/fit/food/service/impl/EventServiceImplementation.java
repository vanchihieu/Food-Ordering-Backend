package vn.edu.iuh.fit.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Events;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.repository.EventRepository;
import vn.edu.iuh.fit.food.service.EventsService;
import vn.edu.iuh.fit.food.service.RestaurantService;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImplementation implements EventsService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Events createEvent(Events event, Long restaurantId) throws InvalidDataException {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        Events createdEvent = new Events();
        createdEvent.setRestaurant(restaurant);
        createdEvent.setImage(event.getImage());
        createdEvent.setStartedAt(event.getStartedAt());
        createdEvent.setEndsAt(event.getEndsAt());
        createdEvent.setLocation(event.getLocation());
        createdEvent.setName(event.getName());

        return eventRepository.save(createdEvent);
    }

    @Override
    public List<Events> findAllEvent() {
        return eventRepository.findAll();
    }

    @Override
    public List<Events> findRestaurantsEvent(Long id) {
        return eventRepository.findEventsByRestaurantId(id);
    }

    @Override
    public void deleteEvent(Long id) throws Exception {
        Events event = findById(id);
        eventRepository.delete(event);
    }

    @Override
    public Events findById(Long id) throws Exception {
        Optional<Events> opt = eventRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("event not found with id " + id);
    }
}
