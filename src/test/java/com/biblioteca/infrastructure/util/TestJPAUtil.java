package com.biblioteca.infrastructure.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe utilitária para gerenciar o EntityManager nos testes de integração.
 */
public class TestJPAUtil {
    
    private static EntityManagerFactory emf;
    
    /**
     * Obtém uma instância do EntityManagerFactory para testes.
     * Utiliza a unidade de persistência "biblioteca-test" configurada para H2 em memória.
     * 
     * @return a instância do EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("biblioteca-test");
        }
        return emf;
    }
    
    /**
     * Cria e retorna uma nova instância do EntityManager.
     * 
     * @return uma nova instância do EntityManager
     */
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    
    /**
     * Fecha o EntityManagerFactory.
     * Deve ser chamado ao final da execução dos testes.
     */
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}
