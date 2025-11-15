package model;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Pedido {

    private Long id;
    private Boolean eliminado;
    private String numero;
    private LocalDate fecha;
    private String clienteNombre;
    private BigDecimal total;
    private String estado;

    // Asociacion 1 a 1: Pedido tiene un Envio
    private Envio envio;

    // Constructor vacio
    public Pedido() {
    }

    // Constructor completo
    public Pedido(Long id, Boolean eliminado, String numero, LocalDate fecha,
                  String clienteNombre, BigDecimal total, String estado, Envio envio) {
        this.id = id;
        this.eliminado = eliminado;
        this.numero = numero;
        this.fecha = fecha;
        this.clienteNombre = clienteNombre;
        this.total = total;
        this.estado = estado;
        this.envio = envio;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", numero='" + numero + '\'' +
                ", fecha=" + fecha +
                ", clienteNombre='" + clienteNombre + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", envio=" + (envio != null ? envio.getId() : "sin envio") +
                '}';
    }
}
