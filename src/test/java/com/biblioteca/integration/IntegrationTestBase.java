package com.biblioteca.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;

import com.biblioteca.infrastructure.util.TestJPAUtil;

/**
 * Classe base para testes de integração.
 * Fornece configuração comum e métodos utilitários para todos os testes de
 * integração.
 */
public abstract class IntegrationTestBase {

    protected EntityManager entityManager;
    protected EntityTransaction transaction;

    /**
     * Configuração executada antes de cada teste.
     * Inicializa o EntityManager, limpa o banco de dados e inicia uma transação.
     */
    @Before
    public void setUp() {
        entityManager = TestJPAUtil.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Limpa o banco de dados para evitar interferência entre testes
        limparBancoDados();

        // Método para configurações específicas de cada teste
        beforeEachTest();
    }

    /**
     * Limpa todas as tabelas do banco de dados.
     */
    private void limparBancoDados() {
        try {
            entityManager.createQuery("DELETE FROM Livro l").executeUpdate();
            entityManager.createQuery("DELETE FROM Autor a").executeUpdate();
            entityManager.createQuery("DELETE FROM Editora e").executeUpdate();
            entityManager.flush();
        } catch (Exception e) {
            // Ignora erros de limpeza, pois podem ocorrer na primeira execução
        }
    }

    /**
     * Método a ser sobrescrito pelos testes específicos para configurações
     * adicionais.
     */
    protected void beforeEachTest() {
        // Implementação vazia por padrão
    }

    /**
     * Limpeza executada após cada teste.
     * Faz rollback da transação e fecha o EntityManager.
     */
    @After
    public void tearDown() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }

        // Método para limpezas específicas de cada teste
        afterEachTest();
    }

    /**
     * Método a ser sobrescrito pelos testes específicos para limpezas adicionais.
     */
    protected void afterEachTest() {
        // Implementação vazia por padrão
    }

    /**
     * Executa um código dentro de uma transação.
     * Útil para operações que precisam ser executadas em uma transação separada.
     * 
     * @param runnable o código a ser executado
     */
    protected void inTransaction(Runnable runnable) {
        EntityManager em = TestJPAUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            runnable.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
