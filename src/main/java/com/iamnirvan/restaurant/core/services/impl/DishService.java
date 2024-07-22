package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.requests.dish.DishCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.dish.DishUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.dish.DishCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.DishRepository;
import com.iamnirvan.restaurant.core.services.IDishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DishService implements IDishService {
    private final DishRepository dishRepository;

    /**
     * Create multiple dish instances using a list of create requests
     * @param requests list of create requests
     * @return list of create responses
     * */
    @Override
    public List<DishCreateResponse> createDish(List<DishCreateRequest> requests) {
        final List<DishCreateResponse> result = new ArrayList<>();

        for (DishCreateRequest request : requests) {
            Dish dish = Dish.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .created(OffsetDateTime.now())
                    .build();
            dishRepository.save(dish);
            log.debug(String.format("Dish created: %s", dish));

            // FIXME: assign portion to the dish when creating it...

            result.add(DishCreateResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
                    .updatedBy(dish.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Update multiple dish instances using a list of update requests
     * @param requests list of update requests
     * @return list of update responses
     * @throws BadRequestException if name is empty
     * @throws BadRequestException if description is empty
     * @throws NotFoundException if dish with id does not exist
     * */
    @Transactional
    @Override
    public List<DishUpdateResponse> updateDish(List<DishUpdateRequest> requests) {
        final List<DishUpdateResponse> result = new ArrayList<>();

        for (DishUpdateRequest request : requests) {
            Dish dish = dishRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", request.getId())));

            if (request.getName() != null) {
                if (request.getName().isEmpty()) {
                    throw new BadRequestException("Name cannot be empty");
                }
                dish.setName(request.getName());
            }

            if (request.getDescription() != null) {
                if (request.getDescription().isEmpty()) {
                    throw new BadRequestException("Description cannot be empty");
                }
                dish.setDescription(request.getDescription());
            }

            dish.setUpdated(OffsetDateTime.now());
            dishRepository.save(dish);
            log.debug(String.format("Dish updated: %s", dish));

            result.add(DishUpdateResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
                    .updatedBy(dish.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Delete multiple dish instances using a list of ids
     * @param ids list of ids
     * @return list of delete responses
     * @throws NotFoundException if dish with id does not exist
     * */
    @Transactional
    @Override
    public List<DishDeleteResponse> deleteDish(List<Long> ids) {
        final List<DishDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", id)));

            dishRepository.delete(dish);
            log.debug(String.format("Dish deleted: %s", dish));

            result.add(DishDeleteResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
                    .updatedBy(dish.getUpdatedBy())
                    .build());
        }
        return result;
    }

    // TODO: implement this... Need to be able to get the portions, reviews, and images....
    @Override
    public List<Dish> getDishes(Long id, Long customerId) {
        return null;
    }
}
