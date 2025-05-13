package dut.gianguhohi.shoppiefood.services;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {
    
    protected abstract JpaRepository<T, ID> getRepository();

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void delete(T entity) {
        getRepository().delete(entity);
    }

    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    protected void validateEntity(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
    }

    protected void validateId(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }
} 