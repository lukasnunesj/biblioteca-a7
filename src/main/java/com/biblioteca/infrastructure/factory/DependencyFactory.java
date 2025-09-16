package com.biblioteca.infrastructure.factory;

import javax.persistence.EntityManager;

import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
import com.biblioteca.application.AutorService;
import com.biblioteca.application.EditoraService;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import com.biblioteca.infrastructure.repositories.AutorRepository;
import com.biblioteca.infrastructure.repositories.EditoraRepository;
import com.biblioteca.infrastructure.repositories.LivroRepository;
import com.biblioteca.application.LivroService;
import com.biblioteca.infrastructure.util.JPAUtil;

public class DependencyFactory {

    private final EntityManager entityManager;

    public DependencyFactory() {
        this.entityManager = JPAUtil.getEntityManager();
    }

    public ILivroService createLivroService() {
        ILivroRepository livroRepository = new LivroRepository(entityManager);
        IAutorService autorService = createAutorService();
        IEditoraService editoraService = createEditoraService();
        return new LivroService(livroRepository, editoraService, autorService);
    }

    public IAutorService createAutorService() {
        return new AutorService(new AutorRepository());
    }

    public IEditoraService createEditoraService() {
        return new EditoraService(new EditoraRepository(entityManager));
    }
}
