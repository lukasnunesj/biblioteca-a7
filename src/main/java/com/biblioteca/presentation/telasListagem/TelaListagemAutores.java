package com.biblioteca.presentation.telasListagem;

import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;

import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.formularios.FormAutores;
import com.biblioteca.presentation.templates.TelaListagemPadrao;
import com.biblioteca.presentation.util.ApiClient;

import com.fasterxml.jackson.core.type.TypeReference;

public class TelaListagemAutores extends TelaListagemPadrao {

    private final ApiClient apiClient;

    public TelaListagemAutores() {
        super("Listagem de Autores");
        this.apiClient = new ApiClient();
        carregar();
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
        FormAutores formAutores = new FormAutores();
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
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            if (JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o autor selecionado?", "Confirmação",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Long id = (Long) tabela.getValueAt(selectedRow, 0);
                    apiClient.delete("/autores/" + id);
                    carregar(); // Recarrega a lista
                } catch (Exception e) {
                    ExceptionHandler.tratar(e, this);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um autor para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void carregar() {
        model.setRowCount(0); // Limpa a tabela
        try {
            List<AutorDTO> autores = apiClient.get("/autores", new TypeReference<List<AutorDTO>>() {});
            for (AutorDTO autor : autores) {
                model.addRow(new Object[]{
                        autor.getId(),
                        autor.getNome(),
                        autor.getCpfcnpj(),
                        autor.getTelefone(),
                        autor.getEmail()
                });
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }
}
