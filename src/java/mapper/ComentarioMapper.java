package mapper;

import modelo.Comentario;
import modelo.Campaña;

import dto.ComentarioDto;
import dto.CampañaDto;
import dto.UsuarioDto;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Usuario;

public class ComentarioMapper {
    public static ComentarioDto comentarioToDto(Comentario comentario) {
        CampañaDto campañaDto = campañaToDto(comentario.getCampaña());
        return new ComentarioDto(
            comentario.getId(),
            comentario.getContenido(),
            comentario.getFechaCreacion(),
            comentario.getFechaActualizacion(),
            comentario.getEstado(),
            campañaDto
            
        );
    }

    // Mapper para convertir de ComentarioDto a Comentario (Entidad)
    public static Comentario dtoToComentario(ComentarioDto comentarioDto) {
        Campaña campaña = dtoToCampaña(comentarioDto.campaña());
        return new Comentario(
            comentarioDto.id(),
            comentarioDto.contenido(),
            comentarioDto.fechaCreacion(),
            comentarioDto.fechaActualizacion(),
            comentarioDto.estado(),
            campaña
        );
    }

    // Mapper para convertir de Campaña a CampañaDto
    public static CampañaDto campañaToDto(Campaña campaña) {
        return new CampañaDto(
            campaña.getId(),
            campaña.getNombre(),
            usuarioToDto(campaña.getUsuario())
        );
    }

    // Mapper para convertir de CampañaDto a Campaña (Entidad)
    public static Campaña dtoToCampaña(CampañaDto campañaDto) {
        return new Campaña(
            campañaDto.id(),
            campañaDto.nombre(),
            dtoToUsuario(campañaDto.usuarioDto())
        );
    }

    // Mapper para convertir de Usuario a UsuarioDto
    public static UsuarioDto usuarioToDto(Usuario usuario) {
        return new UsuarioDto(
            usuario.getId(),
            usuario.getNombre()
        );
    }

    // Mapper para convertir de UsuarioDto a Usuario (Entidad)
    public static Usuario dtoToUsuario(UsuarioDto usuarioDto) {
        return new Usuario(
            usuarioDto.id(),
            usuarioDto.nombre()
        );
    }

    // Mapper para convertir de una lista de Reporte a una lista de ReporteDto
    public static List<ComentarioDto> entityListToDtoList(List<Comentario> entityList) {
        return entityList.stream()
            .map(ComentarioMapper::comentarioToDto)
            .collect(Collectors.toList());
    }

    // Mapper para convertir de una lista de ReporteDto a una lista de Reporte
    public static List<Comentario> dtoListToEntityList(List<ComentarioDto> dtoList) {
        return dtoList.stream()
            .map(ComentarioMapper::dtoToComentario)
            .collect(Collectors.toList());
    }

}
