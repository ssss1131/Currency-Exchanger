package com.ssss.dao;

import java.util.List;

public interface Dao<E> {
    List<E> findAll();

    E save(E entity);

}
