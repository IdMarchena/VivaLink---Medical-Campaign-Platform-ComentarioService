package servicio.serviceImpl;
import dto.CampañaDto;
import dto.ComentarioDto;
import dto.UsuarioDto;
import java.sql.SQLException;
import mapper.ComentarioMapper;
import modelo.Comentario;
import repositorio.ComentarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import servicio.service.ComentarioService;

public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;

    public ComentarioServiceImpl(String tipo) throws SQLException {
        this.comentarioRepository= new ComentarioRepository(tipo);
    }

    @Override
    public void guardar(ComentarioDto comentarioDto) {
        Comentario comentario = ComentarioMapper.dtoToComentario(comentarioDto);
        if(comentario !=null){
            comentarioRepository.guardar(comentario);
        }
    }

    @Override
    public ComentarioDto buscarPorId(int id) {
        if(id<0){
            Comentario comentario = comentarioRepository.buscarPorId(id);
            return (comentario != null) ? ComentarioMapper.comentarioToDto(comentario) : null;
        } else {
            return null;
        }
    }

    @Override
    public List<ComentarioDto> listarTodos() {
        return comentarioRepository.listarTodos()
                .stream()
                .map(ComentarioMapper::comentarioToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean actualizar(ComentarioDto comentarioDto) {
        if(comentarioDto !=null){
            Comentario comentario = ComentarioMapper.dtoToComentario(comentarioDto);
            return comentarioRepository.actualizar(comentario);
        }else {
            return false;
        }
        
    }

    @Override
    public boolean eliminar(int id) {
        if(id>0){
            return comentarioRepository.eliminar(id);
        } else {
            return false;
        }
        
    }

    @Override
    public List<ComentarioDto> buscarPorCampaña(int campañaId) {
        if(campañaId >0){
            return comentarioRepository.buscarPorCampaña(campañaId)
                .stream()
                .map(ComentarioMapper::comentarioToDto)
                .collect(Collectors.toList());
        }else {
            return null;
        }
        
    }

    @Override
    public List<ComentarioDto> buscarPorUsuario(int usuarioId) {
        if(usuarioId >0){
            return comentarioRepository.buscarPorUsuario(usuarioId)
                .stream()
                .map(ComentarioMapper::comentarioToDto)
                .collect(Collectors.toList());

        } else {
            return null;
        }
    }

    @Override
    public List<ComentarioDto> buscarPorCampañaYEstado(int campañaId, String estado) {
        if(campañaId >0 && !"".equals(estado)){
            return comentarioRepository.buscarPorCampañaYEstado(campañaId, estado)
                .stream()
                .map(ComentarioMapper::comentarioToDto)
                .collect(Collectors.toList());
        } else {
            return null;
        }
        
    }

    @Override
    public boolean existeComentarioDeUsuarioEnCampaña(int usuarioId, int campañaId) {
        if(usuarioId >0 && campañaId >0){
            return comentarioRepository.existeComentarioDeUsuarioEnCampaña(usuarioId, campañaId);
        } else {
            return false;
        }
    }

    @Override
    public int contarComentariosPorCampaña(int campañaId) {
        if(campañaId >0){
            return comentarioRepository.contarComentariosPorCampaña(campañaId);
        } else {
            return 0;
        }
        
    }

    @Override
    public List<ComentarioDto> buscarPorContenido(String contenido) {
        if(!"".equals(contenido)){
            return comentarioRepository.buscarPorContenido(contenido)
                .stream()
                .map(ComentarioMapper::comentarioToDto)
                .collect(Collectors.toList());
        } else {
            return null;
        }
        
    }

    @Override
    public List<ComentarioDto> buscarPorEstado(String estado) {
        if(!"".equals(estado)){
            return comentarioRepository.buscarPorEstado(estado)
                .stream()
                .map(ComentarioMapper::comentarioToDto)
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public CampañaDto buscarCampañPorId(int campañaId) {
        if(campañaId >0){
            return ComentarioMapper.campañaToDto(comentarioRepository.buscarCampañaPorId(campañaId));
        }else {
            return null;
        }
    }

    @Override
    public UsuarioDto buscarUsuarioPorId(int UsuarioId) {
        if(UsuarioId >0){
            return ComentarioMapper.usuarioToDto(comentarioRepository.buscarUsuarioPorId(UsuarioId));
        }else {
            return null;
        }
    }
}
