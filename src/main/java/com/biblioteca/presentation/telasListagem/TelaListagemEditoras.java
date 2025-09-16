package com.biblioteca.presentation.telasListagem;

import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.presentation.formularios.FormEditoras;
import com.biblioteca.presentation.templates.TelaListagemPadrao;

public class TelaListagemEditoras extends TelaListagemPadrao {

    private final IEditoraService editoraService;

    public TelaListagemEditoras(IEditoraService editoraService) {
        super("Listagem de Editoras");
        this.editoraService = editoraService;
        carregar(); // Carrega as editoras ao abrir
    }

    @Override
    protected DefaultTableModel configurarTableModel() {
        String[] colunas = { "ID", "Nome", "CNPJ", "Telefone", "Email" };
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
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);  // CNPJ
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);  // Telefone
        tabela.getColumnModel().getColumn(4).setPreferredWidth(250);  // Email
        return tabela;
    }

    @Override
    protected void incluir() {
        FormEditoras formEditoras = new FormEditoras(editoraService);
        JDesktopPane desktopPane = getDesktopPane();
        if (desktopPane != null) {
            desktopPane.add(formEditoras);
            formEditoras.setVisible(true);
        }
    }

    @Override
    protected void editar() {
        // Lógica para editar editora selecionada
    }

    @Override
    protected void excluir() {
        // Lógica para excluir editora selecionada
    }

    @Override
    protected void carregar() {
        model.setRowCount(0); // Limpa a tabela

        List<Editora> editoras = editoraService.buscarTodos();
        for (Editora editora : editoras) {
            model.addRow(new Object[] {
                    editora.getId(),
                    editora.getNome(),
                    editora.getCnpj(),
                    editora.getTelefone(),
                    editora.getEmail()
            });
        }
    }
}
