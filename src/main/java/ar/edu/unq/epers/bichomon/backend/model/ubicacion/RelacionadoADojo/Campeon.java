package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.time.LocalDate;

public class Campeon {

    private Bicho bicho;
    private LocalDate fechaDeInicio;
    private LocalDate fechaDeFin;

    public Campeon(Bicho bichoCampeon, LocalDate fechaDeInicio){

        this.bicho = bichoCampeon;
        this.fechaDeInicio = fechaDeInicio;
        this.fechaDeFin = null;
    }

    public Bicho getBicho() {
        return bicho;
    }

    public LocalDate getFechaDeInicio() {
        return fechaDeInicio;
    }

    public LocalDate getFechaDeFin() {
        return fechaDeFin;
    }

    public void setFechaDeFin(LocalDate fechaDeFin) {
        this.fechaDeFin = fechaDeFin;
    }
}
