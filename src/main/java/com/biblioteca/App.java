package com.biblioteca;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.biblioteca.domain.entities.livro.Livro;

/**
 * Classe principal da aplicação Biblioteca.
 * <p>
 * Esta classe contém o método main que inicia a aplicação e demonstra
 * o uso básico do JPA para persistência de dados.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
public class App {
    /**
     * Método principal que inicia a aplicação.
     * Demonstra a criação de um EntityManager, persistência de um livro
     * e consulta simples ao banco de dados.
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Livro livro = new Livro("Dom Casmurro", "1234567890", LocalDate.now());
        em.persist(livro);
        em.getTransaction().commit();

        Livro encontrado = em.find(Livro.class, livro.getId());
        System.out.println("Livro encontrado: " + encontrado.getTitulo());

        em.close();
        emf.close();
    }
}
