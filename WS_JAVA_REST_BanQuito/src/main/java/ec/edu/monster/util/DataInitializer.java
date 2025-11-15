package ec.edu.monster.util;

import ec.edu.monster.JPAUtil;
import ec.edu.monster.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DataInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            TypedQuery<Long> q = em.createQuery(
                    "SELECT COUNT(u) FROM Usuario u WHERE u.username = :user",
                    Long.class
            );
            q.setParameter("user", "MONSTER");
            Long count = q.getSingleResult();

            if (count == 0L) {
                Usuario admin = new Usuario();
                admin.setUsername("MONSTER");

                // 🔐 Guardar hash de la contraseña MONSTER9
                admin.setPassword(PasswordUtil.hashPassword("MONSTER9"));

                admin.setRol("SUPERADMIN");
                admin.setActivo(true);
                em.persist(admin);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) { }
}
