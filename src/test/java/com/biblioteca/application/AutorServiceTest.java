package com.biblioteca.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;

/**
 * Testes unitários para a classe AutorService.
 * <p>
 * Esta classe testa as funcionalidades do serviço de autores,
 * utilizando mocks para simular o comportamento do repositório.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;
    
    @Mock
    private IAutorRepository autorRepository;
    
    private Autor autor;
    private AutorDTO autorDTO;
    private final Long ID = 1L;
    private final String NOME = "Carlos Drummond";
    private final String CPFCNPJ = "123.456.789-00";
    private final String TELEFONE = "(31) 99999-9999";
    private final String EMAIL = "carlos@exemplo.com";
    
    /**
     * Configura o ambiente de teste antes de cada método de teste.
     * Inicializa os mocks e cria objetos de teste.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        autor = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
        autor.setId(ID);
        
        autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);
    }
    
    /**
     * Testa o método de salvar autor.
     * Verifica se o autor é salvo corretamente e se o repositório é chamado.
     */
    @Test
    public void testSalvar() {
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);
        
        Autor resultado = autorService.salvar(autorDTO);
        
        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        
        verify(autorRepository).save(any(Autor.class));
    }
    
    /**
     * Testa o método de buscar autor por ID quando o autor existe.
     * Verifica se o autor correto é retornado.
     */
    @Test
    public void testBuscarPorId() {
        when(autorRepository.findById(ID)).thenReturn(Optional.of(autor));
        
        Optional<Autor> resultado = autorService.buscarPorId(ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());
        
        verify(autorRepository).findById(ID);
    }
    
    /**
     * Testa o método de buscar autor por ID quando o autor não existe.
     * Verifica se um Optional vazio é retornado.
     */
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(autorRepository.findById(ID)).thenReturn(Optional.empty());
        
        Optional<Autor> resultado = autorService.buscarPorId(ID);
        
        assertFalse(resultado.isPresent());
        
        verify(autorRepository).findById(ID);
    }
    
    
    
    /**
     * Testa o método de buscar todos os autores.
     * Verifica se a lista de autores é retornada corretamente.
     */
    @Test
    public void testBuscarTodos() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.findAll()).thenReturn(autores);
        
        List<Autor> resultado = autorService.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(autorRepository).findAll();
    }
    
    /**
     * Testa o método de remover autor.
     * Verifica se o autor é removido corretamente e se o repositório é chamado.
     */
    @Test
    public void testRemover() {
        when(autorRepository.findById(ID)).thenReturn(Optional.of(autor));
        
        autorService.remover(autorDTO);
        
        verify(autorRepository).findById(ID);
        verify(autorRepository).delete(autor);
    }
}
