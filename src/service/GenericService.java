package service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {

    long insertar(T obj) throws Exception;

    boolean actualizar(T obj) throws Exception;

    boolean eliminar(long id) throws Exception;

    Optional<T> getById(long id) throws Exception;

    List<T> getAll() throws Exception;
}
