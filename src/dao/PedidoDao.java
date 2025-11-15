package dao;

import config.DatabaseConnection;
import model.Pedido;
import model.Envio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PedidoDao implements GenericDao<Pedido> {

    // ======================================
    // Crear (INSERT)
    // ======================================

    @Override
    public long crear(Pedido p) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(p, conn);
        }
    }

    @Override
    public long crear(Pedido p, Connection conn) throws Exception {

        String sql = "INSERT INTO Pedido (eliminado, numero, fecha, cliente_nombre, total, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setBoolean(1, p.getEliminado() != null ? p.getEliminado() : false);
            ps.setString(2, p.getNumero());
            ps.setDate(3, Date.valueOf(p.getFecha()));
            ps.setString(4, p.getClienteNombre());
            ps.setBigDecimal(5, p.getTotal());
            ps.setString(6, p.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo insertar Pedido");
    }

    // ======================================
    // Leer por ID (SELECT)
    // ======================================

    @Override
    public Optional<Pedido> leer(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leer(id, conn);
        }
    }

    @Override
    public Optional<Pedido> leer(long id, Connection conn) throws Exception {

        String sql = "SELECT * FROM Pedido WHERE id = ? AND eliminado = FALSE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Pedido p = mapPedido(rs);

                    // leer si existe envio asociado
                    EnvioDao envioDao = new EnvioDao();
                    Optional<Envio> optEnvio = envioDao.leerPorPedido(id, conn);
                    p.setEnvio(optEnvio.orElse(null));

                    return Optional.of(p);
                }
            }
        }

        return Optional.empty();
    }

    // ======================================
    // Leer todos (SELECT *)
    // ======================================

    @Override
    public List<Pedido> leerTodos() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leerTodos(conn);
        }
    }

    @Override
    public List<Pedido> leerTodos(Connection conn) throws Exception {

        String sql = "SELECT * FROM Pedido WHERE eliminado = FALSE ORDER BY id";

        List<Pedido> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pedido p = mapPedido(rs);

                EnvioDao envioDao = new EnvioDao();
                Optional<Envio> optEnvio = envioDao.leerPorPedido(p.getId(), conn);
                p.setEnvio(optEnvio.orElse(null));

                lista.add(p);
            }
        }

        return lista;
    }

    // ======================================
    // Actualizar (UPDATE)
    // ======================================

    @Override
    public boolean actualizar(Pedido p) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return actualizar(p, conn);
        }
    }

    @Override
    public boolean actualizar(Pedido p, Connection conn) throws Exception {

        String sql = "UPDATE Pedido SET eliminado=?, numero=?, fecha=?, cliente_nombre=?, "
                   + "total=?, estado=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, p.getEliminado());
            ps.setString(2, p.getNumero());
            ps.setDate(3, Date.valueOf(p.getFecha()));
            ps.setString(4, p.getClienteNombre());
            ps.setBigDecimal(5, p.getTotal());
            ps.setString(6, p.getEstado());
            ps.setLong(7, p.getId());

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

        String sql = "UPDATE Pedido SET eliminado = TRUE WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // ======================================
    // Mapper helper (convierte ResultSet → Pedido)
    // ======================================

    private Pedido mapPedido(ResultSet rs) throws Exception {

        Pedido p = new Pedido();

        p.setId(rs.getLong("id"));
        p.setEliminado(rs.getBoolean("eliminado"));
        p.setNumero(rs.getString("numero"));
        p.setFecha(rs.getDate("fecha").toLocalDate());
        p.setClienteNombre(rs.getString("cliente_nombre"));
        p.setTotal(rs.getBigDecimal("total"));
        p.setEstado(rs.getString("estado"));

        return p;
    }
}
