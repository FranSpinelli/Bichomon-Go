package ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;

import java.util.List;

public class DueloHelper implements Estrategia{

    public DueloHelper(){}

    public ResultadoCombate calcularDuelo(Bicho bichoRetador, Dojo lugarDePelea){
        ResultadoCombate datosDeLaBatalla= new ResultadoCombate();

        if(lugarDePelea.getCampeonActual() == null){
            this.casoDojoNoTieneCampeon(bichoRetador, lugarDePelea, datosDeLaBatalla);

        }else {

            this.casoDojoTieneCampeon(bichoRetador, lugarDePelea, datosDeLaBatalla);
        }

        return datosDeLaBatalla;
    }

    public Double getRandomDouble(Double valorMinimoPosible, Double valorMaximoPosible){
        //es publico para poder ser mockeado y testeado, lo ideal es que no lo sea.
        //Retorna un numero random entre valorMinimoPosible y valorMaximoPosible (inclusives)

        Double random = Math.random();
        Double resultado =valorMinimoPosible + random * ((valorMaximoPosible-valorMinimoPosible)+0.1);

        //hago esto por que en algunos casos muy aislados me retornaba un numero levemente mayor/menor a 1.0/0.5
        if(resultado > valorMaximoPosible){resultado = valorMaximoPosible;}
        if(resultado < valorMinimoPosible){resultado = valorMinimoPosible;}

        return resultado;
    }

//PRIVATE FUNCTIONS-----------------------------------------------------------------------------------------------------------------------------------------

    private void casoDojoNoTieneCampeon(Bicho bichoRetador, Dojo lugarDePelea, ResultadoCombate contenedorACompletar) {

        lugarDePelea.setCampeonActual(bichoRetador);
        contenedorACompletar.setGanadorDelDuelo(bichoRetador);

        this.setearEnergiaYXPDeEntrenadorDe(bichoRetador);
    }

    private void casoDojoTieneCampeon(Bicho bichoRetador, Dojo lugarDePelea, ResultadoCombate contenedorACompletar){

        Bicho bichoCampeon = lugarDePelea.getCampeonActual().getBicho();
        Integer cantidadDeAtaquesRealizados = 0;

        while (this.bichosSiguenConEnergia(bichoRetador, bichoCampeon, contenedorACompletar) && (10 > cantidadDeAtaquesRealizados)) {

            this.realizarAtaque(bichoRetador, contenedorACompletar.getListaDeDanhoRecibidoPorBichoCampeon());
            cantidadDeAtaquesRealizados = cantidadDeAtaquesRealizados + 1;

            if (this.bichoCampeonSigueVivo(bichoCampeon, contenedorACompletar)) {

                this.realizarAtaque(bichoCampeon, contenedorACompletar.getListaDeDanhoRecibidoPorBichoRetador());
                cantidadDeAtaquesRealizados = cantidadDeAtaquesRealizados + 1;
            }
        }


        if (!this.bichoCampeonSigueVivo(bichoCampeon, contenedorACompletar)) {
            lugarDePelea.setCampeonActual(bichoRetador);
            contenedorACompletar.setGanadorDelDuelo(bichoRetador);
            contenedorACompletar.setPerdedorDelDuelo(bichoCampeon);
        }else{
            contenedorACompletar.setGanadorDelDuelo(bichoCampeon);
            contenedorACompletar.setPerdedorDelDuelo(bichoRetador);
        }

        setearEnergiaYXPDeEntrenadorDe(bichoCampeon);
        setearEnergiaYXPDeEntrenadorDe(bichoRetador);
    }

    private void realizarAtaque(Bicho bichoAtacante, List<Double> listaConDanhoRecibidoDeBichoAtacado){
        Double danhoGeneradoPorBichoAtacante = bichoAtacante.getEnergia() * this.getRandomDouble(0.5, 1.0);
        listaConDanhoRecibidoDeBichoAtacado.add(danhoGeneradoPorBichoAtacante);
    }

    private void setearEnergiaYXPDeEntrenadorDe(Bicho bicho){
        bicho.setEnergia(bicho.getEnergia() + this.getRandomDouble(1.0,5.0).intValue());
        bicho.getEntrenador().addXp(10);
    }

    private boolean bichoCampeonSigueVivo(Bicho campeonActual, ResultadoCombate datosDeBatalla) {

        return datosDeBatalla.getDanhoTotalRecibidoPorCampeon() < campeonActual.getEnergia();
    }

    private Boolean bichosSiguenConEnergia(Bicho bichoRetador, Bicho campeonActual, ResultadoCombate datosDeBatalla){

        Boolean campeonTieneEnergia = this.bichoCampeonSigueVivo(campeonActual, datosDeBatalla);
        Boolean retadorTieneEnergia = datosDeBatalla.getDanhoTotalRecibidoPorRetador() < bichoRetador.getEnergia();

        return  campeonTieneEnergia && retadorTieneEnergia;

    }
}
