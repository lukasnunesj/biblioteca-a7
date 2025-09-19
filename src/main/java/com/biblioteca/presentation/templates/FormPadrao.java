package com.biblioteca.presentation.templates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.biblioteca.presentation.util.ApiClient;

/**
 * Classe abstrata que define um template para formulários da aplicação.
 * <p>
 * Fornece a estrutura básica e comportamentos comuns para todos os formulários,
 * como layout, botões de ação e gerenciamento de eventos. Classes concretas de
 * formulários devem estender esta classe e implementar os métodos abstratos.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public abstract class FormPadrao extends JInternalFrame {

    /**
     * Painel que contém os botões de ação do formulário.
     */
    protected JPanel painelBotoes;
    
    /**
     * Botão para salvar os dados do formulário.
     */
    protected JButton btnSalvar;
    
    /**
     * Botão para cancelar a operação e fechar o formulário.
     */
    protected JButton btnCancelar;
    
    /**
     * Cliente para comunicação com a API REST.
     */
    protected ApiClient apiClient;

    /**
     * Construtor do formulário padrão.
     *
     * @param title título a ser exibido na barra de título do formulário
     */
    public FormPadrao(String title) {
        super(title, true, true, true, true);
        this.apiClient = new ApiClient();
        initComponents();
    }

    /**
     * Inicializa os componentes do formulário.
     * Configura a tela, o painel de botões, o formulário e os eventos.
     */
    protected void initComponents() {
        configTela();
        configPainelBotoes();
        configFormulario();
        configurarEventos();
    }

    /**
     * Configura as propriedades básicas da tela.
     * Define tamanho, comportamento de fechamento e layout.
     */
    protected void configTela() {
        setSize(720, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * Configura o painel de botões na parte inferior do formulário.
     * Cria os botões de salvar e cancelar e os adiciona ao painel.
     */
    protected void configPainelBotoes() {
        painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Configura os eventos dos botões do formulário.
     * Associa as ações de salvar e cancelar aos respectivos botões.
     */
    protected void configurarEventos() {
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> cancelar());
    }

    /**
     * Método abstrato para configurar o layout e os componentes específicos do formulário.
     * Deve ser implementado pelas classes concretas de formulários.
     */
    protected abstract void configFormulario();

    /**
     * Método abstrato para salvar os dados do formulário.
     * Deve ser implementado pelas classes concretas de formulários.
     */
    protected abstract void salvar();

    /**
     * Cancela a operação e fecha o formulário.
     * Pode ser sobrescrito pelas classes concretas para adicionar comportamento específico.
     */
    protected void cancelar() {
        dispose();
    }

}
