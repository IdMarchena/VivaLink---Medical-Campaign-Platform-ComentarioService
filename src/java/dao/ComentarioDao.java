// src/java/dao/ComentarioDao.java
package dao;

import modelo.Comentario;
import modelo.Usuario;
import modelo.Campaña;
import java.util.List;

public interface ComentarioDao {
    // CRUD básico
    void guardar(Comentario comentario);
    Comentario buscarPorId(int id);
    List<Comentario> listarTodos();
    boolean actualizar(Comentario comentario);
    boolean eliminar(int id);
    
    // Métodos específicos del negocio
    List<Comentario> buscarPorCampaña(int campañaId);
    List<Comentario> buscarPorUsuario(int usuarioId);
    List<Comentario> buscarPorCampañaYEstado(int campañaId, String estado);
    boolean existeComentarioDeUsuarioEnCampaña(int usuarioId, int campañaId);
    int contarComentariosPorCampaña(int campañaId);
    
    // Métodos que mencionaste (si realmente los necesitas)
    List<Comentario> buscarPorContenido(String contenido);
    List<Comentario> buscarPorEstado(String estado);
    
    
    Usuario buscarUsuarioPorId(int id);
    Campaña buscarCampañaPorId(int id);
}