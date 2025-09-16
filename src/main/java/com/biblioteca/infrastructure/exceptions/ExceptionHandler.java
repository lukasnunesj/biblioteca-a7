package com.biblioteca.infrastructure.exceptions;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por centralizar o tratamento de exceções da aplicação.
 */
public class ExceptionHandler {
    
    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());
    
    /**
     * Trata uma exceção exibindo uma mensagem apropriada ao usuário e registrando o erro.
     * 
     * @param e A exceção a ser tratada
     * @param componentePai O componente pai para exibir diálogos
     */
    public static void tratar(Throwable e, JComponent componentePai) {
        if (e instanceof BibliotecaException) {
            tratarBibliotecaException((BibliotecaException) e, componentePai);
        } else {
            tratarExcecaoGenerica(e, componentePai);
        }
    }
    
    /**
     * Trata exceções específicas da aplicação Biblioteca.
     */
    private static void tratarBibliotecaException(BibliotecaException e, JComponent componentePai) {
        String titulo;
        int tipoMensagem;
        
        switch (e.getTipoErro()) {
            case ERRO_VALIDACAO:
                titulo = "Erro de Validação";
                tipoMensagem = JOptionPane.WARNING_MESSAGE;
                logger.log(Level.INFO, "Erro de validação: {0}", e.getMessage());
                break;
                
            case RECURSO_NAO_ENCONTRADO:
                titulo = "Recurso Não Encontrado";
                tipoMensagem = JOptionPane.WARNING_MESSAGE;
                logger.log(Level.INFO, "Recurso não encontrado: {0}", e.getMessage());
                break;
                
            case ERRO_PERSISTENCIA:
                titulo = "Erro de Persistência";
                tipoMensagem = JOptionPane.ERROR_MESSAGE;
                logger.log(Level.SEVERE, "Erro de persistência", e);
                break;
                
            case ERRO_NEGOCIO:
                titulo = "Erro de Regra de Negócio";
                tipoMensagem = JOptionPane.WARNING_MESSAGE;
                logger.log(Level.INFO, "Erro de regra de negócio: {0}", e.getMessage());
                break;
                
            default:
                titulo = "Erro";
                tipoMensagem = JOptionPane.ERROR_MESSAGE;
                logger.log(Level.SEVERE, "Erro não categorizado", e);
                break;
        }
        
        JOptionPane.showMessageDialog(componentePai, e.getMessage(), titulo, tipoMensagem);
    }
    
    /**
     * Trata exceções genéricas não específicas da aplicação.
     */
    private static void tratarExcecaoGenerica(Throwable e, JComponent componentePai) {
        logger.log(Level.SEVERE, "Erro inesperado", e);
        JOptionPane.showMessageDialog(
            componentePai,
            "Ocorreu um erro inesperado: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
