package com.biblioteca.presentation.telasListagem;

import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.formularios.FormEditoras;
import com.biblioteca.presentation.templates.TelaListagemPadrao;
import com.biblioteca.presentation.util.ApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaListagemEditoras extends TelaListagemPadrao {

    private final ApiClient apiClient;

    public TelaListagemEditoras() {
        super("Listagem de Editoras");
        this.apiClient = new ApiClient();
        carregar();
    }

    @Override
    protected DefaultTableModel configurarTableModel() {
        String[] colunas = {"ID", "Nome", "CNPJ", "Telefone", "Email"};
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
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(250);
        return tabela;
    }

    @Override
    protected void incluir() {
        FormEditoras formEditoras = new FormEditoras();
        JDesktopPane desktopPane = getDesktopPane();
        if (desktopPane != null) {
            desktopPane.add(formEditoras);
            formEditoras.setVisible(true);
            formEditoras.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                    carregar();
                }
            });
        }
    }

    @Override
    protected void editar() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tabela.getValueAt(selectedRow, 0);
                EditoraDTO editora = apiClient.get("/editoras/" + id, EditoraDTO.class);
                FormEditoras formEditoras = new FormEditoras(editora);
                JDesktopPane desktopPane = getDesktopPane();
                if (desktopPane != null) {
                    desktopPane.add(formEditoras);
                    formEditoras.setVisible(true);
                    formEditoras.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                        @Override
                        public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                            carregar();
                        }
                    });
                }
            } catch (Exception e) {
                ExceptionHandler.tratar(e, this);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma editora para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void excluir() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            if (JOptionPane.showConfirmDialog(this, "Deseja realmente excluir a editora selecionada?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Long id = (Long) tabela.getValueAt(selectedRow, 0);
                    apiClient.delete("/editoras/" + id);
                    carregar();
                } catch (Exception e) {
                    ExceptionHandler.tratar(e, this);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma editora para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void pesquisar() {
        String termo = txtPesquisa.getText();
        if (termo == null || termo.trim().isEmpty()) {
            carregar();
            return;
        }
        try {
            String url = "/editoras/search?termo=" + java.net.URLEncoder.encode(termo, java.nio.charset.StandardCharsets.UTF_8.toString());
            List<EditoraDTO> editoras = apiClient.get(url, ApiClient.listOf(EditoraDTO.class));
            carregarDados(editoras);
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    private void carregarDados(List<EditoraDTO> editoras) {
        model.setRowCount(0);
        if (editoras == null) return;

        for (EditoraDTO editora : editoras) {
            model.addRow(new Object[]{
                    editora.getId(),
                    editora.getNome(),
                    formatarCnpj(editora.getCnpj()),
                    formatarTelefone(editora.getTelefone()),
                    editora.getEmail()
            });
        }
    }

    private String formatarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) {
            return cnpj;
        }
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() < 10) {
            return telefone;
        }
        if (telefone.length() == 11) {
            return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        return telefone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }

    @Override
    protected void carregar() {
        try {
            List<EditoraDTO> editoras = apiClient.get("/editoras", ApiClient.listOf(EditoraDTO.class));
            carregarDados(editoras);
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }
}
