package dao;

import config.DatabaseConnection;
import model.Envio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnvioDao implements GenericDao<Envio> {

    // ======================================
    // Crear (INSERT)
    // ======================================

    @Override
    public long crear(Envio e) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(e, conn);
        }
    }

    @Override
    public long crear(Envio e, Connection conn) throws Exception {

        String sql = "INSERT INTO Envio (id_pedido, eliminado, tracking, empresa, tipo, costo, "
                   + "fecha_despacho, fecha_estimada, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, e.getIdPedido());
            ps.setBoolean(2, e.getEliminado());
            ps.setString(3, e.getTracking());
            ps.setString(4, e.getEmpresa());
            ps.setString(5, e.getTipo());
            ps.setBigDecimal(6, e.getCosto());
            ps.setDate(7, e.getFechaDespacho() != null ? Date.valueOf(e.getFechaDespacho()) : null);
            ps.setDate(8, e.getFechaEstimada() != null ? Date.valueOf(e.getFechaEstimada()) : null);
            ps.setString(9, e.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo insertar Envio");
    }

    // ======================================
    // Leer por ID (SELECT)
    // ======================================

    @Override
    public Optional<Envio> leer(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leer(id, conn);
        }
    }

    @Override
    public Optional<Envio> leer(long id, Connection conn) throws Exception {

        String sql = "SELECT * FROM Envio WHERE id = ? AND eliminado = FALSE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapEnvio(rs));
                }
            }
        }

        return Optional.empty();
    }

    // ======================================
    // Leer Envio por ID de Pedido (para 1:1)
    // ======================================

    public Optional<Envio> leerPorPedido(long idPedido, Connection conn) throws Exception {

        String sql = "SELECT * FROM Envio WHERE id_pedido = ? AND eliminado = FALSE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapEnvio(rs));
                }
            }
        }

        return Optional.empty();
    }

    // ======================================
    // Leer todos
    // ======================================

    @Override
    public List<Envio> leerTodos() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leerTodos(conn);
        }
    }

    @Override
    public List<Envio> leerTodos(Connection conn) throws Exception {

        String sql = "SELECT * FROM Envio WHERE eliminado = FALSE ORDER BY id";

        List<Envio> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapEnvio(rs));
            }
        }

        return lista;
    }

    // ======================================
    // Actualizar
    // ======================================

    @Override
    public boolean actualizar(Envio e) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return actualizar(e, conn);
        }
    }

    @Override
    public boolean actualizar(Envio e, Connection conn) throws Exception {

        String sql = "UPDATE Envio SET eliminado=?, tracking=?, empresa=?, tipo=?, costo=?, "
                   + "fecha_despacho=?, fecha_estimada=?, estado=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, e.getEliminado());
            ps.setString(2, e.getTracking());
            ps.setString(3, e.getEmpresa());
            ps.setString(4, e.getTipo());
            ps.setBigDecimal(5, e.getCosto());
            ps.setDate(6, e.getFechaDespacho() != null ? Date.valueOf(e.getFechaDespacho()) : null);
            ps.setDate(7, e.getFechaEstimada() != null ? Date.valueOf(e.getFechaEstimada()) : null);
            ps.setString(8, e.getEstado());
            ps.setLong(9, e.getId());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // ======================================
    // Eliminar (baja lógica)
    // ======================================

    @Override
    public boolean eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return eliminar(id, conn);
        }
    }

    @Override
    public boolean eliminar(long id, Connection conn) throws Exception {

        String sql = "UPDATE Envio SET eliminado = TRUE WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // ======================================
    // Mapper: ResultSet → Objeto Envio
    // ======================================

    private Envio mapEnvio(ResultSet rs) throws Exception {

        Envio e = new Envio();

        e.setId(rs.getLong("id"));
        e.setIdPedido(rs.getLong("id_pedido"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setTracking(rs.getString("tracking"));
        e.setEmpresa(rs.getString("empresa"));
        e.setTipo(rs.getString("tipo"));
        e.setCosto(rs.getBigDecimal("costo"));

        Date despacho = rs.getDate("fecha_despacho");
        e.setFechaDespacho(despacho != null ? despacho.toLocalDate() : null);

        Date estimada = rs.getDate("fecha_estimada");
        e.setFechaEstimada(estimada != null ? estimada.toLocalDate() : null);

        e.setEstado(rs.getString("estado"));

        return e;
    }
}
