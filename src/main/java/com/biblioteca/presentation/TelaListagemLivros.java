package com.biblioteca.presentation;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import com.biblioteca.presentation.formularios.FormLivros;
import com.biblioteca.presentation.templates.TelaListagemPadrao;

public class TelaListagemLivros extends TelaListagemPadrao {

    private final ILivroService livroService;

    public TelaListagemLivros(ILivroService livroService) {
        super("Listagem de Livros");
        this.livroService = livroService;
        carregar(); // Carrega os livros ao abrir
    }

    @Override
    protected DefaultTableModel configurarTableModel() {
        String[] colunas = { "ID", "ISBN", "Título", "Autor", "Editora", "Publicação" };
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    @Override
    protected JTable configurarTabela(DefaultTableModel model) {
        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120); // ISBN
        tabela.getColumnModel().getColumn(2).setPreferredWidth(250); // Título
        tabela.getColumnModel().getColumn(3).setPreferredWidth(200); // Autores
        tabela.getColumnModel().getColumn(4).setPreferredWidth(150); // Editora
        tabela.getColumnModel().getColumn(5).setPreferredWidth(120); // Publicação
        return tabela;
    }

    @Override
    protected void incluir() {
        FormLivros formLivros = new FormLivros();
        JDesktopPane desktopPane = getDesktopPane();
        if (desktopPane != null) {
            desktopPane.add(formLivros);
            formLivros.setVisible(true);
        }
    }

    @Override
    protected void editar() {
        // Lógica para editar livro selecionado
    }

    @Override
    protected void excluir() {
        // Lógica para excluir livro selecionado
    }

    @Override
    protected void carregar() {
        // Lógica para carregar/atualizar livros na tabela
    }
}
