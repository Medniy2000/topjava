package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (repository.containsKey(userId)){
                repository.get(userId).put(meal.getId(), meal);
            }else {
                Map<Integer, Meal> mealMap = new HashMap<Integer, Meal>();
                mealMap.put(meal.getId(),meal);
                repository.put(userId,mealMap);
            }
            log.info("save {}", meal);
            return meal;
        }
        // treat case: update, but absent in storage
        log.info("update {}", meal);
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public void delete(int id) {
        Map<Integer, Meal> mealMap = repository.values().stream().
                filter(integerMealMap -> integerMealMap.containsKey(id)).findAny().get();
        log.info("delete {}", mealMap.get(id));
        mealMap.remove(id);
    }

    @Override
    public Meal get(int id) {
        log.info("get id={}", id);
        return  repository.values().stream().
                filter(integerMealMap -> integerMealMap.containsKey(id)).findAny().get().get(id);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        log.info("getAll meals for user {}", userId);
        return repository.get(userId).values();
    }
}

