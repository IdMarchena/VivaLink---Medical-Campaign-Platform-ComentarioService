package servicio.serviceImpl;
import dto.CampanaDto;
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
    private final ComentarioMapper mapper;

    public ComentarioServiceImpl() throws SQLException {
        this.comentarioRepository= new ComentarioRepository();
        this.mapper = new ComentarioMapper();
    }

    @Override
    public void guardar(ComentarioDto comentarioDto) {
        Comentario comentario = mapper.dtoToComentario(comentarioDto);
        if(comentario !=null){
            comentarioRepository.guardar(comentario);
        }
    }

    @Override
    public ComentarioDto buscarPorId(int id) {
        return mapper.comentarioToDto(comentarioRepository.buscarPorId(id));
    }

    @Override
    public List<ComentarioDto> listarTodos() {
        return comentarioRepository.listarTodos()
                .stream()
                .map(mapper::comentarioToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean actualizar(ComentarioDto comentarioDto) {
        if(comentarioDto !=null){
            Comentario comentario = mapper.dtoToComentario(comentarioDto);
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
                .map(mapper::comentarioToDto)
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
                .map(mapper::comentarioToDto)
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
                .map(mapper::comentarioToDto)
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
                .map(mapper::comentarioToDto)
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
                .map(mapper::comentarioToDto)
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public CampanaDto buscarCampañPorId(int campañaId) {
        if(campañaId >0){
            return mapper.campañaToDto(comentarioRepository.buscarCampañaPorId(campañaId));
        }else {
            return null;
        }
    }

    @Override
    public UsuarioDto buscarUsuarioPorId(int UsuarioId) {
        if(UsuarioId >0){
            return mapper.usuarioToDto(comentarioRepository.buscarUsuarioPorId(UsuarioId));
        }else {
            return null;
        }
    }
}
