package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Events;
import vn.edu.iuh.fit.food.response.ApiResponse;
import vn.edu.iuh.fit.food.service.EventsService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Event Controller", description = "Event Controller")
public class EventController {
    @Autowired
    public EventsService eventService;

    @Operation(method = "POST", summary = "Create Event", description = "Create Event")
    @PostMapping("/admin/events/restaurant/{restaurantId}")
    public ResponseEntity<Events> createEvents(@RequestBody Events event,
                                               @PathVariable Long restaurantId) throws InvalidDataException {
        Events createdEvents = eventService.createEvent(event, restaurantId);
        return new ResponseEntity<>(createdEvents, HttpStatus.CREATED);
    }

    @Operation(method = "GET", summary = "Get All Events", description = "Get All Events")
    @GetMapping("/events")
    public ResponseEntity<List<Events>> findAllEvents() throws InvalidDataException {
        List<Events> events = eventService.findAllEvent();
        return new ResponseEntity<>(events, HttpStatus.ACCEPTED);
    }

    @Operation(method = "GET", summary = "Get Restaurant Events", description = "Get Restaurant Events")
    @GetMapping("/admin/events/restaurant/{restaurantId}")
    public ResponseEntity<List<Events>> findRestaurantsEvents(
            @PathVariable Long restaurantId) throws InvalidDataException {
        List<Events> events = eventService.findRestaurantsEvent(restaurantId);
        return new ResponseEntity<>(events, HttpStatus.ACCEPTED);
    }

    @Operation(method = "DELETE", summary = "Delete Events", description = "Delete Events")
    @DeleteMapping("/admin/events/{id}")
    public ResponseEntity<ApiResponse> deleteEvents(@PathVariable Long id) throws Exception {
        eventService.deleteEvent(id);
        ApiResponse res = new ApiResponse("Events Deleted", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
