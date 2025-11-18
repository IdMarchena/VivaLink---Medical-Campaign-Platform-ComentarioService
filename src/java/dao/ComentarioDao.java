// src/java/dao/ComentarioDao.java
package dao;

import modelo.Comentario;
import modelo.Usuario;
import modelo.Campana;
import java.util.List;

public interface ComentarioDao {
    void guardar(Comentario comentario);
    Comentario buscarPorId(int id);
    List<Comentario> listarTodos();
    boolean actualizar(Comentario comentario);
    boolean eliminar(int id);
    List<Comentario> buscarPorCampaña(int campañaId);
    List<Comentario> buscarPorUsuario(int usuarioId);
    List<Comentario> buscarPorCampañaYEstado(int campañaId, String estado);
    boolean existeComentarioDeUsuarioEnCampaña(int usuarioId, int campañaId);
    int contarComentariosPorCampaña(int campañaId);
    List<Comentario> buscarPorContenido(String contenido);
    List<Comentario> buscarPorEstado(String estado);
    
    // Añadir estos métodos que faltan
    Usuario buscarUsuarioPorId(int id);
    Campana buscarCampañaPorId(int id);
}