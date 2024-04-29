package rikkei.academy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGenericService<T, E> {
    T findById(E id);

    Page<T> findAll(Pageable pageable);

    T save(T entity);

    void deleteById(E id);
}
