package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    @Autowired
    private MealService service;

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for User {}", id, userId);
        return service.get(id, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for User {}", id, userId);
        service.delete(id, userId);
    }

    public List<MealWithExceed> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for User {}", userId);
        return MealsUtil.getWithExceeded(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public void update(Meal meal, int id) {
        meal.setId(id);
        int userId = SecurityUtil.authUserId();
        log.info("update {} for User {}", meal, userId);
        service.update(meal, userId);
    }

    public Meal create(Meal meal) {
        meal.setId(null);
        int userId = SecurityUtil.authUserId();
        log.info("create {} for User {}", meal, userId);
        return service.save(meal, userId);
    }



}