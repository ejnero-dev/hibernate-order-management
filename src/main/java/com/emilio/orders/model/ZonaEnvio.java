/**
 * Clase que representa una zona de envío en un sistema de acceso a datos.
 */
package com.emilio.orders.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Zonas_Envio")
public class ZonaEnvio {
    /**
     * Identificador único de la zona de envío.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private int idZona;
    
    /**
     * Nombre descriptivo de la zona de envío.
     */
    @Column(name = "nombre_zona", nullable = false, length = 100)
    private String nombreZona;
    
    /**
     * Tarifa asociada al envío a esta zona.
     */
    @Column(name = "tarifa_envio", nullable = false)
    private double tarifaEnvio;

    /**
     * Constructor que inicializa una nueva instancia de ZonaEnvio con los valores proporcionados.
     *
     * @param idZona        El identificador único de la zona de envío.
     * @param nombreZona    El nombre descriptivo de la zona de envío.
     * @param tarifaEnvio   La tarifa asociada al envío a esta zona.
     */
    public ZonaEnvio(int idZona, String nombreZona, double tarifaEnvio) {
        this.idZona = idZona;
        this.nombreZona = nombreZona;
        this.tarifaEnvio = tarifaEnvio;
    }

    /**
     * Constructor vacío que crea una instancia de ZonaEnvio sin inicializar valores.
     */
    public ZonaEnvio() {
    }

    /**
     * Obtiene el identificador único de la zona de envío.
     *
     * @return El identificador único de la zona de envío.
     */
    public int getIdZona() {
        return idZona;
    }

    /**
     * Establece el identificador único de la zona de envío.
     *
     * @param idZona El nuevo identificador único de la zona de envío.
     */
    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    /**
     * Obtiene el nombre descriptivo de la zona de envío.
     *
     * @return El nombre descriptivo de la zona de envío.
     */
    public String getNombreZona() {
        return nombreZona;
    }

    /**
     * Establece el nombre descriptivo de la zona de envío, realizando validaciones.
     *
     * @param nombreZona El nuevo nombre descriptivo de la zona de envío.
     */
    public void setNombreZona(String nombreZona) {
        if (nombreZona == null || nombreZona.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de zona no puede estar vacío");
        }
        if (nombreZona.length() > 100) {
            throw new IllegalArgumentException("El nombre de zona no puede exceder 100 caracteres");
        }
        this.nombreZona = nombreZona.trim();
    }

    /**
     * Obtiene la tarifa asociada al envío a esta zona.
     *
     * @return La tarifa asociada al envío a esta zona.
     */
    public double getTarifaEnvio() {
        return tarifaEnvio;
    }

    /**
     * Establece la tarifa asociada al envío a esta zona, realizando validaciones.
     *
     * @param tarifaEnvio La nueva tarifa asociada al envío a esta zona.
     */
    public void setTarifaEnvio(double tarifaEnvio) {
        if (tarifaEnvio < 0) {
            throw new IllegalArgumentException("La tarifa no puede ser negativa");
        }
        if (tarifaEnvio > 999.99) {
            throw new IllegalArgumentException("La tarifa excede el máximo permitido");
        }
        this.tarifaEnvio = tarifaEnvio;
    }

    /**
     * Devuelve una representación en forma de cadena del objeto ZonaEnvio.
     *
     * @return Una cadena que describe la zona de envío.
     */
    @Override
    public String toString() {
        return String.format("%s (Tarifa: %.2f€)", nombreZona, tarifaEnvio);
    }
}