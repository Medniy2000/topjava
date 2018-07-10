package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.util.Collection;

public interface MealService {

    Meal get(int id, int userId) throws NotFoundException;

    void delete(int id, int userId) throws NotFoundException;

    Collection<Meal> getAll(int userId);

    Meal update(Meal meal, int userId) throws NotFoundException;

    Meal save(Meal meal, int userId);
}