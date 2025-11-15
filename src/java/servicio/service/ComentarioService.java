package servicio.service;
import dto.CampanaDto;
import dto.ComentarioDto;
import dto.UsuarioDto;
import java.util.List;

public interface ComentarioService {

    void guardar(ComentarioDto comentarioDto);

    ComentarioDto buscarPorId(int id);

    List<ComentarioDto> listarTodos();

    boolean actualizar(ComentarioDto comentarioDto);

    boolean eliminar(int id);

    List<ComentarioDto> buscarPorCampaña(int campañaId);

    List<ComentarioDto> buscarPorUsuario(int usuarioId);

    List<ComentarioDto> buscarPorCampañaYEstado(int campañaId, String estado);

    boolean existeComentarioDeUsuarioEnCampaña(int usuarioId, int campañaId);

    int contarComentariosPorCampaña(int campañaId);

    List<ComentarioDto> buscarPorContenido(String contenido);

    List<ComentarioDto> buscarPorEstado(String estado);
    
    CampanaDto buscarCampañPorId(int campañaId);
    
    UsuarioDto buscarUsuarioPorId(int UsuarioId);
}
