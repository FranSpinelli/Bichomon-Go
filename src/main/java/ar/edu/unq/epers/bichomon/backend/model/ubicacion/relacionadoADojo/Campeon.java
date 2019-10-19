package ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class Campeon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Bicho bicho;
    private LocalDate fechaDeInicio;
    private LocalDate fechaDeFin;

    public Campeon(){}

    public Campeon(Bicho bichoCampeon, LocalDate fechaDeInicio){

        this.bicho = bichoCampeon;
        this.fechaDeInicio = fechaDeInicio;
        this.fechaDeFin = null;
    }

    public Bicho getBicho() {
        return bicho;
    }

    public void setBicho(Bicho bicho){ this.bicho = bicho; }

    public LocalDate getFechaDeInicio() {
        return fechaDeInicio;
    }

    public void setFechaDeInicio(LocalDate fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public LocalDate getFechaDeFin() {
        return fechaDeFin;
    }

    public void setFechaDeFin(LocalDate fechaDeFin) {
        this.fechaDeFin = fechaDeFin;
    }

    public Integer cantidadDiasDeCampeonato(){
        if(this.fechaDeFin == null){
            return (new Long(ChronoUnit.DAYS.between(this.fechaDeInicio, LocalDate.now()))).intValue();
        }else{
            return (new Long(ChronoUnit.DAYS.between(this.fechaDeInicio, this.fechaDeFin))).intValue();
        }
    }
}
