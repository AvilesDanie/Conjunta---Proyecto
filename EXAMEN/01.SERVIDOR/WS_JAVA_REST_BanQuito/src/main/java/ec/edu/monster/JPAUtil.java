/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster;

/**
 *
 * @author danie
 */

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory emf;

    private static synchronized EntityManagerFactory getEmf() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("banquitoPU");
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEmf().createEntityManager();
    }
}
