package ar.edu.unq.epers.bichomon.backend;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({
        "ar.edu.unq.epers.bichomon.backend.dao.impl",
        "ar.edu.unq.epers.bichomon.backend.model.bicho",
        "ar.edu.unq.epers.bichomon.backend.model.especie",
        "ar.edu.unq.epers.bichomon.backend.model.serviceData",
        "ar.edu.unq.epers.bichomon.backend.model.condicionDeEvolucion",
})

public class AllTests {
}
