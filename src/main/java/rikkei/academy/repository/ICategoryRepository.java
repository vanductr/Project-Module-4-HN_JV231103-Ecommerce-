package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
