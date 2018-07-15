package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal getted = service.get(ADMIN_MEAL1.getId(), ADMIN_ID);
        Assert.assertEquals(ADMIN_MEAL1, getted);
    }

    @Test
    public void delete() {
        Assert.assertEquals(service.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1));
        service.delete(ADMIN_MEAL1.getId(), ADMIN_ID);
        Assert.assertEquals(service.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL2));
    }

    @Test
    public void getBetweenDateTimes() {
        Assert.assertEquals(Arrays.asList(MEAL3, MEAL2, MEAL1),
                service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30), USER_ID));
    }

    @Test
    public void getAll() {
        Assert.assertEquals(service.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1));
    }

    @Test
    public void update() {
        Meal mealToUpdate = MealTestData.getUpdated();
        service.update(mealToUpdate, ADMIN_ID);
        Assert.assertEquals(service.get(mealToUpdate.getId(), ADMIN_ID), mealToUpdate);
    }


    @Test
    public void create() {
        Meal newMeal = MealTestData.getCreated();
        Meal created = service.create(newMeal, ADMIN.getId());
        newMeal.setId(created.getId());
        Assert.assertEquals(created, newMeal);
    }
}