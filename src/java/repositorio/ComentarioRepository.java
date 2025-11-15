/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import dao.ComentarioDao;
import factory.ComentarioDaoFactory;
import java.sql.SQLException;
import java.util.List;
import modelo.Campana;
import modelo.Comentario;
import modelo.Usuario;
/**
 *
 * @author Usuario
 */
public class ComentarioRepository {
    private final ComentarioDao comentarioDao;
    
    public ComentarioRepository(String tipo) throws SQLException{
        this.comentarioDao= ComentarioDaoFactory.dao(tipo);
    }
    
    public void guardar(Comentario comentario){
        comentarioDao.guardar(comentario);
    }
    public Comentario buscarPorId(int id){
        return comentarioDao.buscarPorId(id);
    }
    public List<Comentario> listarTodos(){
        return comentarioDao.listarTodos();
    }
    public boolean actualizar(Comentario comentario){
        return comentarioDao.actualizar(comentario);
    }
    public boolean eliminar(int id){
        return comentarioDao.eliminar(id);
    }
    
    public List<Comentario> buscarPorCampaña(int campañaId){
        return comentarioDao.buscarPorCampaña(campañaId);
    }
    public List<Comentario> buscarPorUsuario(int usuarioId){
        return comentarioDao.buscarPorUsuario(usuarioId);
    }
    public List<Comentario> buscarPorCampañaYEstado(int campañaId, String estado){
        return comentarioDao.buscarPorCampañaYEstado(campañaId, estado);
    }
    public boolean existeComentarioDeUsuarioEnCampaña(int usuarioId, int campañaId){
        return comentarioDao.existeComentarioDeUsuarioEnCampaña(usuarioId, campañaId);
    }
    public int contarComentariosPorCampaña(int campañaId){
        return comentarioDao.contarComentariosPorCampaña(campañaId);
    }
    
    public List<Comentario> buscarPorContenido(String contenido){
        return comentarioDao.buscarPorContenido(contenido);
    }
    public List<Comentario> buscarPorEstado(String estado){
        return comentarioDao.buscarPorEstado(estado);
    }
    public Usuario buscarUsuarioPorId(int usuarioId){
        return comentarioDao.buscarUsuarioPorId(usuarioId);
    }
    public Campana buscarCampañaPorId(int campañaId){
        return comentarioDao.buscarCampañaPorId(campañaId);
    }
}
