package com.biblioteca.infrastructure.util;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Classe utilitária para validação de campos de formulário
 */
public class ValidacaoUtil {

    /**
     * Representa um campo a ser validado
     */
    public static class CampoValidacao {
        private final JTextComponent campo;
        private final String nomeCampo;
        private final List<Validacao> validacoes;

        public CampoValidacao(JTextComponent campo, String nomeCampo) {
            this.campo = campo;
            this.nomeCampo = nomeCampo;
            this.validacoes = new ArrayList<>();
        }

        /**
         * Adiciona validação de campo obrigatório
         */
        public CampoValidacao obrigatorio() {
            validacoes.add(new Validacao(
                texto -> !texto.isBlank(),
                "O campo '" + nomeCampo + "' é obrigatório."
            ));
            return this;
        }

        /**
         * Adiciona validação de tamanho mínimo
         */
        public CampoValidacao tamanhoMinimo(int tamanho) {
            validacoes.add(new Validacao(
                texto -> texto.length() >= tamanho,
                "O campo '" + nomeCampo + "' deve ter pelo menos " + tamanho + " caracteres."
            ));
            return this;
        }

        /**
         * Adiciona validação de formato de email
         */
        public CampoValidacao email() {
            validacoes.add(new Validacao(
                texto -> texto.isBlank() || texto.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"),
                "O campo '" + nomeCampo + "' deve ser um email válido."
            ));
            return this;
        }

        /**
         * Adiciona validação personalizada
         */
        public CampoValidacao validacao(Predicate<String> validador, String mensagemErro) {
            validacoes.add(new Validacao(validador, mensagemErro));
            return this;
        }

        /**
         * Executa todas as validações configuradas para o campo
         */
        public boolean validar(List<String> erros) {
            String texto = campo.getText();
            boolean valido = true;

            for (Validacao validacao : validacoes) {
                if (!validacao.validador.test(texto)) {
                    erros.add(validacao.mensagemErro);
                    valido = false;
                    break;
                }
            }

            return valido;
        }
    }

    /**
     * Classe interna que representa uma validação
     */
    private static class Validacao {
        private final Predicate<String> validador;
        private final String mensagemErro;

        public Validacao(Predicate<String> validador, String mensagemErro) {
            this.validador = validador;
            this.mensagemErro = mensagemErro;
        }
    }

    /**
     * Classe para construir e executar validações de formulário
     */
    public static class ValidadorFormulario {
        private final List<CampoValidacao> campos;
        private final JComponent componentePai;

        public ValidadorFormulario(JComponent componentePai) {
            this.campos = new ArrayList<>();
            this.componentePai = componentePai;
        }

        /**
         * Adiciona um campo para validação
         */
        public CampoValidacao campo(JTextComponent campo, String nomeCampo) {
            CampoValidacao campoValidacao = new CampoValidacao(campo, nomeCampo);
            campos.add(campoValidacao);
            return campoValidacao;
        }

        /**
         * Executa todas as validações e exibe mensagens de erro se necessário
         */
        public boolean validar() {
            List<String> erros = new ArrayList<>();
            boolean valido = true;

            for (CampoValidacao campo : campos) {
                if (!campo.validar(erros)) {
                    valido = false;
                    break;
                }
            }

            if (!valido && !erros.isEmpty()) {
                JOptionPane.showMessageDialog(
                    componentePai,
                    erros.get(0),
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            return valido;
        }
    }

    /**
     * Cria um novo validador de formulário
     */
    public static ValidadorFormulario validador(JComponent componentePai) {
        return new ValidadorFormulario(componentePai);
    }
}
