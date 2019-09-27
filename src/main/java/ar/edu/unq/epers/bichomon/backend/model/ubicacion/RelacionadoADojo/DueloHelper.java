package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.List;

public class DueloHelper {
    /*todo REFACTOR DEL DUELO*/

    public ContenedorConDatosDelDuelo realizarDuelo(Bicho bichoRetador, Dojo lugarDePelea){
        ContenedorConDatosDelDuelo datosDeLaBatalla= new ContenedorConDatosDelDuelo();

        if(lugarDePelea.getCampeonActual() == null){
            this.casoDojoNoTieneCampeon(bichoRetador, lugarDePelea, datosDeLaBatalla);

        }else {

            this.casoDojoTieneCampeon(bichoRetador, lugarDePelea, datosDeLaBatalla);
        }

        return datosDeLaBatalla;
    }

    public Double getRandom(Double valorMinimoPosible, Double valorMaximoPosible){
        //es publico para poder ser mockeado y testeado, lo ideal es que no lo sea.
        //Retorna un numero random entre valorMinimoPosible y valorMaximoPosible (inclusives)

        Double random = Math.random();
        Double resultado =valorMinimoPosible + random * ((valorMaximoPosible-valorMinimoPosible)+0.1);

        //hago esto por que en algunos casos muy aislados me retornaba un numero levemente mayor/menor a 1.0/0.5
        if(resultado > 1.00000){resultado = 0.99;}
        if(resultado < 0.5){resultado = 0.52;}

        return resultado;
    }

//PRIVATE FUNCTIONS-----------------------------------------------------------------------------------------------------------------------------------------

    private void casoDojoNoTieneCampeon(Bicho bichoRetador, Dojo lugarDePelea, ContenedorConDatosDelDuelo contenedorACompletar) {

        lugarDePelea.setCampeonActual(bichoRetador);
        contenedorACompletar.setGanadorDelDuelo(bichoRetador);

        this.setearEnergiaYXPDeEntrenadorDe(bichoRetador);
    }

    private void casoDojoTieneCampeon(Bicho bichoRetador, Dojo lugarDePelea, ContenedorConDatosDelDuelo contenedorACompletar){

        Bicho bichoCampeon = lugarDePelea.getCampeonActual();
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
        }else{
            contenedorACompletar.setGanadorDelDuelo(bichoCampeon);
        }

        setearEnergiaYXPDeEntrenadorDe(bichoCampeon);
        setearEnergiaYXPDeEntrenadorDe(bichoRetador);
    }

    private void realizarAtaque(Bicho bichoAtacante, List<Double> listaConDanhoRecibidoDeBichoAtacado){
        Double danhoGeneradoPorBichoAtacante = bichoAtacante.getEnergia() * this.getRandom(0.5, 1.0);
        listaConDanhoRecibidoDeBichoAtacado.add(danhoGeneradoPorBichoAtacante);
    }

    private void setearEnergiaYXPDeEntrenadorDe(Bicho bicho){
        bicho.setEnergia(bicho.getEnergia() + this.getRandom(1.0,5.0).intValue());
        bicho.getEntrenador().addXp(10);
    }

    private boolean bichoCampeonSigueVivo(Bicho campeonActual, ContenedorConDatosDelDuelo datosDeBatalla) {

        return datosDeBatalla.getDanhoTotalRecibidoPorCampeon() < campeonActual.getEnergia();
    }

    private Boolean bichosSiguenConEnergia(Bicho bichoRetador, Bicho campeonActual, ContenedorConDatosDelDuelo datosDeBatalla){

        Boolean campeonTieneEnergia = this.bichoCampeonSigueVivo(campeonActual, datosDeBatalla);
        Boolean retadorTieneEnergia = datosDeBatalla.getDanhoTotalRecibidoPorRetador() < bichoRetador.getEnergia();

        return  campeonTieneEnergia && retadorTieneEnergia;

    }
}
