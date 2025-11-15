package service;

import config.DatabaseConnection;
import dao.EnvioDao;
import model.Envio;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class EnvioService implements GenericService<Envio> {

    private final EnvioDao envioDao = new EnvioDao();

    // ============================================
    // Insertar Envio
    // ============================================

    @Override
    public long insertar(Envio e) throws Exception {

        validarEnvio(e);

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            // Regla 1:1 -> no debe existir otro envío con ese id_pedido
            Optional<Envio> existente = envioDao.leerPorPedido(e.getIdPedido(), conn);
            if (existente.isPresent()) {
                conn.rollback();
                throw new Exception("El pedido ya tiene un envío asociado.");
            }

            long id = envioDao.crear(e, conn);

            conn.commit();
            return id;

        } catch (Exception ex) {
            throw new Exception("Error al insertar Envio: " + ex.getMessage(), ex);
        }
    }

    // ============================================
    // Actualizar Envio
    // ============================================

    @Override
    public boolean actualizar(Envio e) throws Exception {

        validarEnvio(e);

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            boolean ok = envioDao.actualizar(e, conn);

            conn.commit();
            return ok;

        } catch (Exception ex) {
            throw new Exception("Error al actualizar Envio: " + ex.getMessage(), ex);
        }
    }

    // ============================================
    // Eliminar (baja lógica)
    // ============================================

    @Override
    public boolean eliminar(long id) throws Exception {

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            boolean ok = envioDao.eliminar(id, conn);

            conn.commit();
            return ok;

        } catch (Exception ex) {
            throw new Exception("Error al eliminar Envio: " + ex.getMessage(), ex);
        }
    }

    // ============================================
    // Obtener por ID
    // ============================================

    @Override
    public Optional<Envio> getById(long id) throws Exception {
        return envioDao.leer(id);
    }

    // ============================================
    // Listar todos
    // ============================================

    @Override
    public List<Envio> getAll() throws Exception {
        return envioDao.leerTodos();
    }

    // ============================================
    // Validaciones
    // ============================================

    private void validarEnvio(Envio e) throws Exception {

        if (e.getIdPedido() <= 0) {
            throw new Exception("Debe indicar un ID de Pedido válido.");
        }

        if (e.getTracking() == null || e.getTracking().isBlank()) {
            throw new Exception("El tracking es obligatorio.");
        }

        if (e.getEmpresa() == null || e.getEmpresa().isBlank()) {
            throw new Exception("La empresa de envío es obligatoria.");
        }

        if (e.getTipo() == null || e.getTipo().isBlank()) {
            throw new Exception("El tipo de envío es obligatorio.");
        }

        if (e.getCosto() == null || e.getCosto().doubleValue() < 0) {
            throw new Exception("El costo debe ser mayor o igual a 0.");
        }

        if (e.getEstado() == null || e.getEstado().isBlank()) {
            throw new Exception("El estado del envío es obligatorio.");
        }
    }
}
