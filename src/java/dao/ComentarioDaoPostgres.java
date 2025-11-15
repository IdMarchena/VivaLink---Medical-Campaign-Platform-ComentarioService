package dao;

import modelo.Comentario;
import modelo.Campana;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import dao.connection.DatabaseConnection;
import factory.DatabaseConnectionFactory;

public class ComentarioDaoPostgres implements ComentarioDao {

    private final Connection conn;

    public ComentarioDaoPostgres(String tipoDb) throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnectionFactory.connection(tipoDb);
        this.conn = dbConnection.getConection();
    }
    
    @Override
    public void guardar(Comentario comentario) {
        String sql = """
            INSERT INTO comentarios (contenido, fecha_creacion, fecha_actualizacion, estado, id_campaña)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id;
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, comentario.getContenido());
            ps.setObject(2, comentario.getFechaCreacion());
            ps.setObject(3, comentario.getFechaActualizacion());
            ps.setString(4, comentario.getEstado());
            ps.setInt(5, comentario.getCampaña().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                comentario.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println("Error guardando comentario: " + e.getMessage());
        }
    }

    @Override
    public Comentario buscarPorId(int id) {

        String sql = """
            SELECT 
                co.id AS coId, 
                co.contenido AS coContenido,
                co.fecha_creacion AS fechaCreacion,
                co.fecha_actualizacion AS fechaActualizacion,
                co.estado AS coEstado,
                ca.id AS caId,
                ca.nombre AS caNombre,
                u.id AS uId,
                u.nombre AS uNombre
            FROM comentarios co
            INNER JOIN campañas ca ON co.id_campaña = ca.id
            INNER JOIN usuarios u ON ca.id_usuario = u.id
            WHERE co.id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("uId"),
                    rs.getString("uNombre")
                );
                Campana campaña = new Campana(
                    rs.getInt("caId"),
                    rs.getString("caNombre"),
                    usuario
                );
                return new Comentario(
                    rs.getInt("coId"),
                    rs.getString("coContenido"),
                    rs.getObject("fechaCreacion", LocalDate.class),
                    rs.getObject("fechaActualizacion", LocalDate.class),
                    rs.getString("coEstado"),
                    campaña
                );
            }
        } catch (SQLException e) {
            System.err.println("Error buscando comentario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Comentario> listarTodos() {
        List<Comentario> lista = new ArrayList<>();
        String sql = """
            SELECT 
                co.id AS coId, 
                co.contenido AS coContenido,
                co.fecha_creacion AS fechaCreacion,
                co.fecha_actualizacion AS fechaActualizacion,
                co.estado AS coEstado,
                ca.id AS caId,
                ca.nombre AS caNombre,
                u.id AS uId,
                u.nombre AS uNombre
            FROM comentarios co
            INNER JOIN campañas ca ON co.id_campaña = ca.id
            INNER JOIN usuarios u ON ca.id_usuario = u.id
            ORDER BY co.id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("uId"),
                    rs.getString("uNombre")
                );
                Campana campaña = new Campana(
                    rs.getInt("caId"),
                    rs.getString("caNombre"),
                    usuario
                );
                Comentario comentario = new Comentario(
                    rs.getInt("coId"),
                    rs.getString("coContenido"),
                    rs.getObject("fechaCreacion", LocalDate.class),
                    rs.getObject("fechaActualizacion", LocalDate.class),
                    rs.getString("coEstado"),
                    campaña
                );
                lista.add(comentario);
            }
        } catch (SQLException e) {
            System.err.println("Error listando comentarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Comentario comentario) {
        String sql = """
            UPDATE comentarios
            SET contenido = ?, fecha_actualizacion = ?, estado = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, comentario.getContenido());
            ps.setObject(2, comentario.getFechaActualizacion());
            ps.setString(3, comentario.getEstado());
            ps.setInt(4, comentario.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando comentario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM comentarios WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando comentario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Comentario> buscarPorCampaña(int campañaId) {
        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT id FROM comentarios WHERE id_campaña = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, campañaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando comentarios por campaña: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Comentario> buscarPorUsuario(int usuarioId) {
        List<Comentario> lista = new ArrayList<>();
        String sql = """
            SELECT co.id
            FROM comentarios co
            INNER JOIN campañas ca ON co.id_campaña = ca.id
            WHERE ca.id_usuario = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando comentarios por usuario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Comentario> buscarPorCampañaYEstado(int campañaId, String estado) {
        List<Comentario> lista = new ArrayList<>();
        String sql = """
            SELECT id 
            FROM comentarios
            WHERE id_campaña = ? AND estado = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, campañaId);
            ps.setString(2, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando comentarios por campaña y estado: " + e.getMessage());
        }
        return lista;
    }
   
    @Override
    public boolean existeComentarioDeUsuarioEnCampaña(int usuarioId, int campañaId) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM comentarios co
            INNER JOIN campañas ca ON co.id_campaña = ca.id
            WHERE ca.id_usuario = ? AND ca.id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, campañaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando comentario de usuario en campaña: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int contarComentariosPorCampaña(int campañaId) {
        String sql = "SELECT COUNT(*) AS total FROM comentarios WHERE id_campaña = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, campañaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error contando comentarios: " + e.getMessage());
        }
        return 0;
    }
    @Override
    public List<Comentario> buscarPorContenido(String contenido) {
        List<Comentario> lista = new ArrayList<>();

        String sql = """
            SELECT id 
            FROM comentarios
            WHERE contenido ILIKE ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + contenido + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando comentarios por contenido: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Comentario> buscarPorEstado(String estado) {
        List<Comentario> lista = new ArrayList<>();
        String sql = """
            SELECT id 
            FROM comentarios
            WHERE estado = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando comentarios por estado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Usuario buscarUsuarioPorId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Campana buscarCampañaPorId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
