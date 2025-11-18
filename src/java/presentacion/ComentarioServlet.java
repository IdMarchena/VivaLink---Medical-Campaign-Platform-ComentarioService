package presentacion;

import servicio.service.ComentarioService;
import servicio.serviceImpl.ComentarioServiceImpl;
import dto.ComentarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CampanaDto;
import dto.UsuarioDto;
import java.time.LocalDate;

@WebServlet(name = "ComentarioServlet", urlPatterns = {"/ComentarioServlet"})
public class ComentarioServlet extends HttpServlet {

    private final ComentarioService comentarioService;
    private final Gson gson;

    // Constructor
    public ComentarioServlet() throws SQLException {
        this.comentarioService = new ComentarioServiceImpl();
        
        // Configurar Gson con el adaptador para LocalDate
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setDateFormat("yyyy-MM-dd")
            .create();
    }

    // Método para procesar las peticiones
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

        // Configurar respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Filtra las acciones según el parámetro "action"
            if ("guardar".equals(action)) {
                guardarComentario(request, response);
            } else if ("listarTodos".equals(action)) {
                listarTodosComentarios(request, response);
            } else if ("buscarPorId".equals(action)) {
                buscarComentarioPorId(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarComentario(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarComentario(request, response);
            } else if ("buscarPorCampaña".equals(action)) {
                buscarComentariosPorCampaña(request, response);
            } else if ("buscarPorUsuario".equals(action)) {
                buscarComentariosPorUsuario(request, response);
            } else if ("buscarPorCampañaYEstado".equals(action)) {
                buscarComentariosPorCampañaYEstado(request, response);
            } else if ("existeComentarioDeUsuarioEnCampaña".equals(action)) {
                existeComentarioDeUsuarioEnCampaña(request, response);
            } else if ("contarComentariosPorCampaña".equals(action)) {
                contarComentariosPorCampaña(request, response);
            } else if ("buscarPorContenido".equals(action)) {
                buscarComentariosPorContenido(request, response);
            } else if ("buscarPorEstado".equals(action)) {
                buscarComentariosPorEstado(request, response);
            } else if ("buscarUsuario".equals(action)) {
                buscarUsuario(request, response);
            } else if ("buscarCampaña".equals(action)) {
                buscarCampaña(request, response);
            } else {
                // Acción no reconocida
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Acción no reconocida: " + action));
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error interno del servidor: " + e.getMessage()));
        }
    }

    // Acción para guardar un comentario
    private void guardarComentario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contenido = request.getParameter("contenido");
        String campañaIdStr = request.getParameter("campañaId");
        String usuarioIdStr = request.getParameter("usuarioId");

        try {
            // Validar parámetros requeridos
            if (contenido == null || contenido.isEmpty() || 
                campañaIdStr == null || campañaIdStr.isEmpty() || 
                usuarioIdStr == null || usuarioIdStr.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos son requeridos"));
                return;
            }

            int campañaId = Integer.parseInt(campañaIdStr);
            int usuarioId = Integer.parseInt(usuarioIdStr);

            // Verificar que la campaña existe
            CampanaDto campaña = comentarioService.buscarCampañPorId(campañaId);
            if (campaña == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("La campaña no existe"));
                return;
            }

            // Verificar que el usuario existe
            UsuarioDto usuario = comentarioService.buscarUsuarioPorId(usuarioId);
            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("El usuario no existe"));
                return;
            }
            ComentarioDto comentarioDto = new ComentarioDto(
                0,
                contenido,
                LocalDate.now(),
                null,
                "ACTIVO", // estado actualizado
                campaña
            );            

            comentarioService.guardar(comentarioDto);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson("Comentario creado con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de campaña o usuario inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al guardar comentario: " + e.getMessage()));
        }
    }

    // Acción para listar todos los comentarios
    private void listarTodosComentarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ComentarioDto> comentarios = comentarioService.listarTodos();
            response.getWriter().write(gson.toJson(comentarios));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar comentarios: " + e.getMessage()));
        }
    }

    // Acción para buscar comentario por ID
    private void buscarComentarioPorId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro id es requerido"));
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            ComentarioDto comentario = comentarioService.buscarPorId(id);
            
            if (comentario != null) {
                response.getWriter().write(gson.toJson(comentario));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Comentario no encontrado"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de comentario inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar comentario: " + e.getMessage()));
        }
    }

    // Acción para actualizar un comentario
    private void actualizarComentario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        String contenido = request.getParameter("contenido");

        try {
            int id = Integer.parseInt(idStr);
            
            if (contenido == null || contenido.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("El contenido es requerido"));
                return;
            }

            ComentarioDto comentarioExistente = comentarioService.buscarPorId(id);
            if (comentarioExistente == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Comentario no encontrado"));
                return;
            }

            // Crear nuevo DTO con los datos actualizados (los records son inmutables)
            ComentarioDto comentarioActualizado = new ComentarioDto(
                comentarioExistente.id(),
                contenido,
                comentarioExistente.fechaCreacion(),
                LocalDate.now(), // fechaActualizacion actualizada
                "EDITADO", // estado actualizado
                comentarioExistente.campaña()
            );

            boolean actualizado = comentarioService.actualizar(comentarioActualizado);
            
            if (actualizado) {
                response.getWriter().write(gson.toJson("Comentario actualizado con éxito"));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(gson.toJson("Error al actualizar comentario"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de comentario inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al actualizar comentario: " + e.getMessage()));
        }
    }

    // Acción para eliminar un comentario
    private void eliminarComentario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");

        try {
            int id = Integer.parseInt(idStr);
            boolean eliminado = comentarioService.eliminar(id);
            
            if (eliminado) {
                response.getWriter().write(gson.toJson("Comentario eliminado con éxito"));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Comentario no encontrado"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de comentario inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al eliminar comentario: " + e.getMessage()));
        }
    }

    // Acción para buscar comentarios por campaña
    private void buscarComentariosPorCampaña(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String campañaIdStr = request.getParameter("campañaId");
        
        if (campañaIdStr == null || campañaIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro campañaId es requerido"));
            return;
        }
        
        try {
            int campañaId = Integer.parseInt(campañaIdStr);
            List<ComentarioDto> comentarios = comentarioService.buscarPorCampaña(campañaId);
            response.getWriter().write(gson.toJson(comentarios));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de campaña inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar comentarios por campaña: " + e.getMessage()));
        }
    }

    // Acción para buscar comentarios por usuario
    private void buscarComentariosPorUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usuarioIdStr = request.getParameter("usuarioId");
        
        if (usuarioIdStr == null || usuarioIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro usuarioId es requerido"));
            return;
        }
        
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            List<ComentarioDto> comentarios = comentarioService.buscarPorUsuario(usuarioId);
            response.getWriter().write(gson.toJson(comentarios));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de usuario inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar comentarios por usuario: " + e.getMessage()));
        }
    }

    // Acción para buscar comentarios por campaña y estado
    private void buscarComentariosPorCampañaYEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String campañaIdStr = request.getParameter("campañaId");
        String estado = request.getParameter("estado");
        
        if (campañaIdStr == null || campañaIdStr.isEmpty() || estado == null || estado.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("Los parámetros campañaId y estado son requeridos"));
            return;
        }
        
        try {
            int campañaId = Integer.parseInt(campañaIdStr);
            List<ComentarioDto> comentarios = comentarioService.buscarPorCampañaYEstado(campañaId, estado);
            response.getWriter().write(gson.toJson(comentarios));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de campaña inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar comentarios por campaña y estado: " + e.getMessage()));
        }
    }

    // Acción para verificar si existe comentario de usuario en campaña
    private void existeComentarioDeUsuarioEnCampaña(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usuarioIdStr = request.getParameter("usuarioId");
        String campañaIdStr = request.getParameter("campañaId");
        
        if (usuarioIdStr == null || usuarioIdStr.isEmpty() || campañaIdStr == null || campañaIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("Los parámetros usuarioId y campañaId son requeridos"));
            return;
        }
        
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            int campañaId = Integer.parseInt(campañaIdStr);
            boolean existe = comentarioService.existeComentarioDeUsuarioEnCampaña(usuarioId, campañaId);
            response.getWriter().write(gson.toJson(existe));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de usuario o campaña inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al verificar existencia de comentario: " + e.getMessage()));
        }
    }

    // Acción para contar comentarios por campaña
    private void contarComentariosPorCampaña(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String campañaIdStr = request.getParameter("campañaId");
        
        if (campañaIdStr == null || campañaIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro campañaId es requerido"));
            return;
        }
        
        try {
            int campañaId = Integer.parseInt(campañaIdStr);
            int cantidad = comentarioService.contarComentariosPorCampaña(campañaId);
            response.getWriter().write(gson.toJson(cantidad));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de campaña inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al contar comentarios: " + e.getMessage()));
        }
    }

    // Acción para buscar comentarios por contenido
    private void buscarComentariosPorContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contenido = request.getParameter("contenido");
        
        if (contenido == null || contenido.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro contenido es requerido"));
            return;
        }
        
        try {
            List<ComentarioDto> comentarios = comentarioService.buscarPorContenido(contenido);
            response.getWriter().write(gson.toJson(comentarios));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar comentarios por contenido: " + e.getMessage()));
        }
    }

    // Acción para buscar comentarios por estado
    private void buscarComentariosPorEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String estado = request.getParameter("estado");
        
        if (estado == null || estado.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro estado es requerido"));
            return;
        }
        
        try {
            List<ComentarioDto> comentarios = comentarioService.buscarPorEstado(estado);
            response.getWriter().write(gson.toJson(comentarios));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar comentarios por estado: " + e.getMessage()));
        }
    }

    // Nuevo método: Buscar usuario por ID
    private void buscarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usuarioIdStr = request.getParameter("id");
        
        if (usuarioIdStr == null || usuarioIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro id es requerido"));
            return;
        }
        
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            UsuarioDto usuario = comentarioService.buscarUsuarioPorId(usuarioId);
            
            if (usuario != null) {
                response.getWriter().write(gson.toJson(usuario));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Usuario no encontrado"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de usuario inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar usuario: " + e.getMessage()));
        }
    }

    // Nuevo método: Buscar campaña por ID
    private void buscarCampaña(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String campañaIdStr = request.getParameter("id");
        
        if (campañaIdStr == null || campañaIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro id es requerido"));
            return;
        }
        
        try {
            int campañaId = Integer.parseInt(campañaIdStr);
            CampanaDto campaña = comentarioService.buscarCampañPorId(campañaId);
            
            if (campaña != null) {
                response.getWriter().write(gson.toJson(campaña));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Campaña no encontrada"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de campaña inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar campaña: " + e.getMessage()));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}