// src/java/modelo/Comentario.java
package modelo;

import java.time.LocalDate;



public class Comentario {
    private int id;
    private String contenido;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
    private String estado;
    private Campana campaña;
    
    // Constructores
    public Comentario(String estado,LocalDate fecha) {
        this.fechaCreacion = fecha;
        this.estado =estado;
    }
    
    public Comentario(int id,String contenido,LocalDate fechaCreacion,LocalDate fechaActualizacion,String estado, Campana campaña) {
        this.id=id;
        this.contenido = contenido;
        this.fechaCreacion=fechaCreacion;
        this.fechaActualizacion=fechaActualizacion;
        this.estado=estado;
        this.campaña = campaña;
    }

    @Override
    public String toString() {
        return "Comentario{" + "id=" + getId() + ", contenido=" + getContenido() + ", fechaCreacion=" + getFechaCreacion() + ", fechaActualizacion=" + getFechaActualizacion() + ", estado=" + getEstado() + ", campa\u00f1a=" + getCampaña() + ", usuario="  + '}';
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the fechaCreacion
     */
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the fechaActualizacion
     */
    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    /**
     * @param fechaActualizacion the fechaActualizacion to set
     */
    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the campaña
     */
    public Campana getCampaña() {
        return campaña;
    }

    /**
     * @param campaña the campaña to set
     */
    public void setCampaña(Campana campaña) {
        this.campaña = campaña;
    }

    /**
     * @return the usuario
     */



}