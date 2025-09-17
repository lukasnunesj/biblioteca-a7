package com.biblioteca.presentation.telasListagem;

import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;

import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.formularios.FormEditoras;
import com.biblioteca.presentation.templates.TelaListagemPadrao;
import com.biblioteca.presentation.util.ApiClient;

import com.fasterxml.jackson.core.type.TypeReference;

public class TelaListagemEditoras extends TelaListagemPadrao {

    private final ApiClient apiClient;

    public TelaListagemEditoras() {
        super("Listagem de Editoras");
        this.apiClient = new ApiClient();
        carregar();
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
        FormEditoras formEditoras = new FormEditoras();
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
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            if (JOptionPane.showConfirmDialog(this, "Deseja realmente excluir a editora selecionada?", "Confirmação",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Long id = (Long) tabela.getValueAt(selectedRow, 0);
                    apiClient.delete("/editoras/" + id);
                    carregar(); // Recarrega a lista
                } catch (Exception e) {
                    ExceptionHandler.tratar(e, this);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma editora para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void carregar() {
        model.setRowCount(0); // Limpa a tabela
        try {
            List<EditoraDTO> editoras = apiClient.get("/editoras", new TypeReference<List<EditoraDTO>>() {});
            for (EditoraDTO editora : editoras) {
                model.addRow(new Object[]{
                        editora.getId(),
                        editora.getNome(),
                        editora.getCnpj(),
                        editora.getTelefone(),
                        editora.getEmail()
                });
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }
}
