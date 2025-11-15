/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package dto;

import java.time.LocalDate;
import modelo.Campana;

/**
 *
 * @author Usuario
 */
public record ComentarioDto(int id,
                            String contenido,
                            LocalDate fechaCreacion,
                            LocalDate fechaActualizacion,
                            String estado, 
                            CampanaDto campaña) {

}
