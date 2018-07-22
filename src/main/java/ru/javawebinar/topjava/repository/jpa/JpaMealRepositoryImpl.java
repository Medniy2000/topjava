package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()){
            em.persist(meal);
            return meal;
        }else {
            return em.merge(meal);
        }

    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=:d1 AND m.user.id=:d2");

        return query.
                setParameter("d1", id).
                setParameter("d2", userId).
                executeUpdate() != 0;

    }

    @Override
    public Meal get(int id, int userId) {
        TypedQuery<Meal> query   = em.createQuery("SELECT m FROM Meal m WHERE m.id=:d1 AND m.user.id=:d2", Meal.class);

        return  query.setParameter("d1", id).
                setParameter("d2", userId).getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        TypedQuery<Meal>  query = em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:d1 ORDER BY date_time DESC", Meal.class);

        query.setParameter("d1", userId);

        return  query.getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {

        TypedQuery<Meal>  query = em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:d3 " +
                "AND date_time BETWEEN :d1 AND :d2 ORDER BY date_time DESC", Meal.class);

        query.setParameter("d1",startDate);
        query.setParameter("d2",endDate);
        query.setParameter("d3", userId);

        return query.getResultList();
    }
}