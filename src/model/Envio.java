package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Envio {

    private Long id;
    private Long idPedido;
    private Boolean eliminado;
    private String tracking;
    private String empresa;
    private String tipo;
    private BigDecimal costo;
    private LocalDate fechaDespacho;
    private LocalDate fechaEstimada;
    private String estado;

    // Constructor vacio
    public Envio() {
    }

    // Constructor completo
    public Envio(Long id, Long idPedido, Boolean eliminado, String tracking, String empresa,
                 String tipo, BigDecimal costo, LocalDate fechaDespacho,
                 LocalDate fechaEstimada, String estado) {
        this.id = id;
        this.idPedido = idPedido;
        this.eliminado = eliminado;
        this.tracking = tracking;
        this.empresa = empresa;
        this.tipo = tipo;
        this.costo = costo;
        this.fechaDespacho = fechaDespacho;
        this.fechaEstimada = fechaEstimada;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public LocalDate getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(LocalDate fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public LocalDate getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(LocalDate fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Envio{" +
                "id=" + id +
                ", idPedido=" + idPedido +
                ", eliminado=" + eliminado +
                ", tracking='" + tracking + '\'' +
                ", empresa='" + empresa + '\'' +
                ", tipo='" + tipo + '\'' +
                ", costo=" + costo +
                ", fechaDespacho=" + fechaDespacho +
                ", fechaEstimada=" + fechaEstimada +
                ", estado='" + estado + '\'' +
                '}';
    }
}


// ACOMODAR EL TOSTRING PARA QUE DE DATOS DEL PEDIDO CUANDO PEDIS INFORMACION DEL ENVIO

