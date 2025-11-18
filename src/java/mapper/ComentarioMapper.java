package mapper;

import modelo.Comentario;
import modelo.Campana;

import dto.ComentarioDto;
import dto.CampanaDto;
import dto.UsuarioDto;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Usuario;

public class ComentarioMapper {
    public ComentarioDto comentarioToDto(Comentario comentario) {
        CampanaDto campañaDto = campañaToDto(comentario.getCampaña());
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
    public Comentario dtoToComentario(ComentarioDto comentarioDto) {
        Campana campaña = dtoToCampaña(comentarioDto.campaña());
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
    public CampanaDto campañaToDto(Campana campaña) {
        return new CampanaDto(
            campaña.getId(),
            campaña.getNombre(),
            usuarioToDto(campaña.getUsuario())
        );
    }

    // Mapper para convertir de CampañaDto a Campaña (Entidad)
    public Campana dtoToCampaña(CampanaDto campañaDto) {
        return new Campana(
            campañaDto.id(),
            campañaDto.nombre(),
            dtoToUsuario(campañaDto.usuarioDto())
        );
    }

    // Mapper para convertir de Usuario a UsuarioDto
    public UsuarioDto usuarioToDto(Usuario usuario) {
        return new UsuarioDto(
            usuario.getId(),
            usuario.getNombre()
        );
    }

    // Mapper para convertir de UsuarioDto a Usuario (Entidad)
    public Usuario dtoToUsuario(UsuarioDto usuarioDto) {
        return new Usuario(
            usuarioDto.id(),
            usuarioDto.nombre()
        );
    }

    // Mapper para convertir de una lista de Reporte a una lista de ReporteDto
    public List<ComentarioDto> entityListToDtoList(List<Comentario> entityList) {
        return entityList.stream()
            .map(this::comentarioToDto)
            .collect(Collectors.toList());
    }

    // Mapper para convertir de una lista de ReporteDto a una lista de Reporte
    public List<Comentario> dtoListToEntityList(List<ComentarioDto> dtoList) {
        return dtoList.stream()
            .map(this::dtoToComentario)
            .collect(Collectors.toList());
    }

}
