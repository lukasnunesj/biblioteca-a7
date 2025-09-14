package com.biblioteca.presentation.formularios;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.presentation.templates.FormPadrao;

public class FormLivros extends FormPadrao {
    private JTextField txtTitulo, txtIsbn, txtDataPublicacao, txtAutores, txtEditora, txtDescricao;
    private JButton btnBuscarPorIsbn;

    public FormLivros() {
        super("Formulário de Livros");
    }

    @Override
    protected void initComponents() {
        configTela();
        configPainelBotoes();
        configFormulario();
        configurarEventos();
    }

    @Override
    protected void salvar() {
        dispose();
    }

    @Override
    protected void configFormulario() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelPrincipal.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTitulo = new JTextField(30);
        painelPrincipal.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel painelIsbn = new JPanel(new BorderLayout());
        txtIsbn = new JTextField();
        btnBuscarPorIsbn = new JButton("Buscar");
        btnBuscarPorIsbn.addActionListener(e -> buscarPorIsbn());
        painelIsbn.add(txtIsbn, BorderLayout.CENTER);
        painelIsbn.add(btnBuscarPorIsbn, BorderLayout.EAST);
        painelPrincipal.add(painelIsbn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelPrincipal.add(new JLabel("Data de Publicação:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtDataPublicacao = new JTextField(30);
        painelPrincipal.add(txtDataPublicacao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painelPrincipal.add(new JLabel("Autores:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtAutores = new JTextField(30);
        painelPrincipal.add(txtAutores, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        painelPrincipal.add(new JLabel("Editora:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtEditora = new JTextField(30);
        painelPrincipal.add(txtEditora, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        painelPrincipal.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtDescricao = new JTextField(30);
        painelPrincipal.add(txtDescricao, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private void buscarPorIsbn() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um ISBN para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: Implementar busca na API OpenLibrary
        JOptionPane.showMessageDialog(this, "Funcionalidade de busca por ISBN será implementada na próxima fase.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
