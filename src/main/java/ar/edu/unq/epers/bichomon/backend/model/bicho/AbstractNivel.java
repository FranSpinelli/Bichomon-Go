package ar.edu.unq.epers.bichomon.backend.model.bicho;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractNivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public AbstractNivel() {}

    public abstract int getNivel();

    public abstract int getNroBichos();

    public abstract int getLimite();

    public abstract AbstractNivel eval(int xp);
}
