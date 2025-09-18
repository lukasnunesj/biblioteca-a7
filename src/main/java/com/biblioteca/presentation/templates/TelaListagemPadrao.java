package com.biblioteca.presentation.templates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class TelaListagemPadrao extends JInternalFrame {

    protected JTable tabela;
    protected DefaultTableModel model;
    protected JButton btnNovo;
    protected JButton btnEditar;
    protected JButton btnExcluir;
    protected JButton btnImportarIsbn;
    protected JButton btnAtualizar;
    protected JButton btnFechar;

    public TelaListagemPadrao(String title) {
        super(title, true, true, true, true);
        initComponents();
    }

    protected void initComponents() {
        configTela();
        configTabela();
        configPainelBotoes();
        configurarEventos();
    }

    protected void configTela() {
        setSize(720, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    protected void configTabela() {
        model = configurarTableModel();
        tabela = configurarTabela(model);

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);
    }

    protected void configPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNovo = new JButton("Novo");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnImportarIsbn = new JButton("Importar por ISBN");
        btnAtualizar = new JButton("Atualizar");
        btnFechar = new JButton("Fechar");

        panel.add(btnNovo);
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(btnImportarIsbn);
        panel.add(btnAtualizar);
        panel.add(btnFechar);

        add(panel, BorderLayout.SOUTH);
    }

    protected void configurarEventos() {
        btnNovo.addActionListener(e -> incluir());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnImportarIsbn.addActionListener(e -> importarPorIsbn());
        btnAtualizar.addActionListener(e -> carregar());
        btnFechar.addActionListener(e -> dispose());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean temSelecao = tabela.getSelectedRow() != -1;
                btnEditar.setEnabled(temSelecao);
                btnExcluir.setEnabled(temSelecao);
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() != -1) {
                    editar();
                }
            }
        });

        // Desabilitar botões de ação que dependem de seleção
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }

    protected abstract DefaultTableModel configurarTableModel();

    protected abstract JTable configurarTabela(DefaultTableModel model);

    protected abstract void incluir();

    protected abstract void editar();

    protected abstract void excluir();

    protected abstract void carregar();

    protected void importarPorIsbn() {
        // A ser implementado pelas subclasses
    }

}
