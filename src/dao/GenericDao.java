package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    // Crear
    long crear(T obj) throws Exception;
    long crear(T obj, Connection conn) throws Exception;

    // Leer por ID
    Optional<T> leer(long id) throws Exception;
    Optional<T> leer(long id, Connection conn) throws Exception;

    // Leer todos
    List<T> leerTodos() throws Exception;
    List<T> leerTodos(Connection conn) throws Exception;

    // Actualizar
    boolean actualizar(T obj) throws Exception;
    boolean actualizar(T obj, Connection conn) throws Exception;

    // Eliminar (baja logica)
    boolean eliminar(long id) throws Exception;
    boolean eliminar(long id, Connection conn) throws Exception;
}
