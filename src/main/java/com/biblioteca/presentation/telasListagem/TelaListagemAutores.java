package com.biblioteca.presentation.telasListagem;

import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.presentation.formularios.FormAutores;
import com.biblioteca.presentation.templates.TelaListagemPadrao;

public class TelaListagemAutores extends TelaListagemPadrao {

    private final IAutorService autorService;

    public TelaListagemAutores(IAutorService autorService) {
        super("Listagem de Autores");
        this.autorService = autorService;
        carregar(); // Carrega os autores ao abrir
    }

    @Override
    protected DefaultTableModel configurarTableModel() {
        String[] colunas = { "ID", "Nome", "CPF/CNPJ", "Telefone", "Email" };
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
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(250);  // Nome
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);  // CPF/CNPJ
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);  // Telefone
        tabela.getColumnModel().getColumn(4).setPreferredWidth(250);  // Email
        return tabela;
    }

    @Override
    protected void incluir() {
        FormAutores formAutores = new FormAutores(autorService);
        JDesktopPane desktopPane = getDesktopPane();
        if (desktopPane != null) {
            desktopPane.add(formAutores);
            formAutores.setVisible(true);
        }
    }

    @Override
    protected void editar() {
        // Lógica para editar autor selecionado
    }

    @Override
    protected void excluir() {
        // Lógica para excluir autor selecionado
    }

    @Override
    protected void carregar() {
        model.setRowCount(0); // Limpa a tabela

        List<Autor> autores = autorService.buscarTodos();
        for (Autor autor : autores) {
            model.addRow(new Object[] {
                    autor.getId(),
                    autor.getNome(),
                    autor.getCpfcnpj(),
                    autor.getTelefone(),
                    autor.getEmail()
            });
        }
    }
}
