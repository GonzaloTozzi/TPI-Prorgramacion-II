package service;

import config.DatabaseConnection;
import dao.EnvioDao;
import dao.PedidoDao;
import model.Envio;
import model.Pedido;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class PedidoService implements GenericService<Pedido> {

    private final PedidoDao pedidoDao = new PedidoDao();
    private final EnvioDao envioDao = new EnvioDao();

    // ============================================
    // Insertar Pedido (con o sin Envio asociado)
    // ============================================

    @Override
    public long insertar(Pedido pedido) throws Exception {

        validarPedido(pedido);

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            long idPedido = pedidoDao.crear(pedido, conn);

            // si tiene envío asociado, lo creamos
            if (pedido.getEnvio() != null) {

                Envio e = pedido.getEnvio();
                e.setIdPedido(idPedido);

                // regla 1:1 -> no puede existir un envio previo para este pedido
                Optional<Envio> envioExistente = envioDao.leerPorPedido(idPedido, conn);
                if (envioExistente.isPresent()) {
                    conn.rollback();
                    throw new Exception("Este pedido ya tiene un envío asociado.");
                }

                envioDao.crear(e, conn);
            }

            conn.commit();
            return idPedido;

        } catch (Exception e) {
            throw new Exception("Error al insertar Pedido: " + e.getMessage(), e);
        }
    }

    // ============================================
    // Actualizar Pedido
    // ============================================

    @Override
    public boolean actualizar(Pedido p) throws Exception {

        validarPedido(p);

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            boolean ok = pedidoDao.actualizar(p, conn);
            conn.commit();
            return ok;

        } catch (Exception e) {
            throw new Exception("Error al actualizar Pedido: " + e.getMessage(), e);
        }
    }

    // ============================================
    // Eliminar Pedido (baja lógica)
    // ============================================

    @Override
    public boolean eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            boolean ok = pedidoDao.eliminar(id, conn);
            conn.commit();
            return ok;

        } catch (Exception e) {
            throw new Exception("Error al eliminar Pedido: " + e.getMessage(), e);
        }
    }

    // ============================================
    // Obtener un pedido por ID
    // ============================================

    @Override
    public Optional<Pedido> getById(long id) throws Exception {
        return pedidoDao.leer(id);
    }

    // ============================================
    // Listar todos los pedidos
    // ============================================

    @Override
    public List<Pedido> getAll() throws Exception {
        return pedidoDao.leerTodos();
    }

    // ============================================
    // Validaciones
    // ============================================

    private void validarPedido(Pedido p) throws Exception {

        if (p.getNumero() == null || p.getNumero().isBlank()) {
            throw new Exception("El número de pedido es obligatorio.");
        }

        if (p.getClienteNombre() == null || p.getClienteNombre().isBlank()) {
            throw new Exception("El nombre del cliente es obligatorio.");
        }

        if (p.getTotal() == null || p.getTotal().doubleValue() < 0) {
            throw new Exception("El total del pedido debe ser mayor o igual a 0.");
        }

        if (p.getEstado() == null || p.getEstado().isBlank()) {
            throw new Exception("El estado del pedido es obligatorio.");
        }
    }
}
