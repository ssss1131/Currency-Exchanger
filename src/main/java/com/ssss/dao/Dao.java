package com.ssss.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E> {
    List<E> findAll();

    Optional<E> save(E entity);

}
