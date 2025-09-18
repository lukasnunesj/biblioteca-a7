package com.biblioteca.presentation.templates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.biblioteca.presentation.util.ApiClient;

public abstract class FormPadrao extends JInternalFrame {

    protected JButton btnSalvar;
    protected JButton btnCancelar;
    protected ApiClient apiClient;

    public FormPadrao(String title) {
        super(title, true, true, true, true);
        this.apiClient = new ApiClient();
        initComponents();
    }

    protected void initComponents() {
        configTela();
        configPainelBotoes();
        configFormulario();
        configurarEventos();
    }

    protected void configTela() {
        setSize(720, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    protected void configPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel, BorderLayout.SOUTH);
    }

    protected void configurarEventos() {
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> cancelar());
    }

    protected abstract void configFormulario();

    protected abstract void salvar();

    protected void cancelar() {
        dispose();
    }

}
