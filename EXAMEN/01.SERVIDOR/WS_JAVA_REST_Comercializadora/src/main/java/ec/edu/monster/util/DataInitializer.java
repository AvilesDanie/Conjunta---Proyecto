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
        // Ejecutar en un hilo separado para no bloquear el inicio de la aplicación
        new Thread(() -> {
            try {
                // Esperar 8 segundos para que:
                // 1. El contexto web termine de inicializarse
                // 2. JPA cree las tablas automáticamente
                // 3. El EntityManager esté listo
                Thread.sleep(8000);
                
                System.out.println("🔄 Iniciando verificación de usuario admin...");
                
                EntityManager em = null;
                try {
                    em = JPAUtil.getEntityManager();
                    
                    // Verificar que la tabla existe antes de hacer la consulta
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
                        admin.setPassword(PasswordUtil.hashPassword("MONSTER9"));
                        admin.setRol("SUPERADMIN");
                        admin.setActivo(true);
                        em.persist(admin);
                        
                        System.out.println("✅ Usuario MONSTER creado exitosamente");
                    } else {
                        System.out.println("ℹ️ Usuario MONSTER ya existe en la base de datos");
                    }

                    em.getTransaction().commit();
                } catch (Exception e) {
                    if (em != null && em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    System.err.println("❌ Error al inicializar datos:");
                    e.printStackTrace();
                } finally {
                    if (em != null) {
                        em.close();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Inicialización interrumpida");
            }
        }).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) { }
}
