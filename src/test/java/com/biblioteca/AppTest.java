package com.biblioteca;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Classe de teste básica para a aplicação.
 * <p>
 * Esta classe contém testes unitários simples para verificar
 * a configuração do ambiente de testes.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class AppTest
        extends TestCase {
    /**
     * Cria um novo caso de teste com o nome especificado.
     *
     * @param testName nome do caso de teste
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * Cria e retorna uma suíte de testes contendo todos os métodos
     * de teste desta classe.
     *
     * @return a suíte de testes a ser executada
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Teste simples para verificar se o ambiente de testes está configurado corretamente.
     * Este teste sempre passa, pois apenas verifica se true é true.
     */
    public void testApp() {
        assertTrue(true);
    }
}
