package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions;

public class DojoNoUtilizado extends RuntimeException {
    public DojoNoUtilizado(String mensaje) {
        super(mensaje);
    }
}
