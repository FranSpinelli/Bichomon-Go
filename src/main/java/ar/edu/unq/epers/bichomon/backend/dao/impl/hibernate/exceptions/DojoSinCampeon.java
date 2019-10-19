package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions;

public class DojoSinCampeon extends RuntimeException {
    public DojoSinCampeon(String mensaje) {
        super(mensaje);
    }
}
