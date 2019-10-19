package ar.edu.unq.epers.bichomon.backend.model.ubicacion;


import java.util.Random;

public class Aleatorio {
    public Integer nextInt(Integer hasta){
        return (new Random()).nextInt(hasta);
    }
}
