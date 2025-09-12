package com.biblioteca;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.biblioteca.entity.Livro;

/**
 * Hello world!
 *
 */
public class App {
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
