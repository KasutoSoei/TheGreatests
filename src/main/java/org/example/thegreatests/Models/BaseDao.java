package org.example.thegreatests.Models;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class BaseDao<T, ID> {
    private final Dao<T, ID> dao;

    public BaseDao(ConnectionSource source, Class<T> clazz) {
        try {
            this.dao = DaoManager.createDao(source, clazz);
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to create DAO for class: " + clazz.getName(), e);
        }
    }

    public void create(T entity) throws SQLException {
        dao.create(entity);
    }

    public T findById(ID id) throws SQLException {
        return dao.queryForId(id);
    }

    public List<T> findAll() throws SQLException {
        return dao.queryForAll();
    }

    public void update(T entity) throws SQLException {
        dao.update(entity);
    }

    public void delete(T entity) throws SQLException {
        dao.delete(entity);
    }

    public void deleteById(ID id) throws SQLException {
        T entity = findById(id);
        if (entity != null) {
            delete(entity);
        }
    }
}