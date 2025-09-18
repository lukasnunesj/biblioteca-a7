package com.biblioteca.presentation.telasListagem;

import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.formularios.FormAutores;
import com.biblioteca.presentation.templates.TelaListagemPadrao;
import com.biblioteca.presentation.util.ApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class TelaListagemAutores extends TelaListagemPadrao {

    private final ApiClient apiClient;

    public TelaListagemAutores() {
        super("Listagem de Autores");
        this.apiClient = new ApiClient();
        carregar();
    }

    @Override
    protected DefaultTableModel configurarTableModel() {
        String[] colunas = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Email"};
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
        tabela.getColumnModel().getColumn(1).setPreferredWidth(250); // Nome
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150); // CPF/CNPJ
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120); // Telefone
        tabela.getColumnModel().getColumn(4).setPreferredWidth(250); // Email
        return tabela;
    }

    @Override
    protected void incluir() {
        FormAutores formAutores = new FormAutores();
        JDesktopPane desktopPane = getDesktopPane();
        if (desktopPane != null) {
            desktopPane.add(formAutores);
            formAutores.setVisible(true);

            formAutores.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
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
                Map<String, Object> autorMap = apiClient.get("/autores/" + id, ApiClient.mapOf(String.class, Object.class));

                if (autorMap == null) {
                    throw new Exception("Não foi possível carregar os dados do autor.");
                }

                AutorDTO autor = new AutorDTO();
                if (autorMap.get("id") instanceof Number) {
                    autor.setId(((Number) autorMap.get("id")).longValue());
                } else if (autorMap.get("id") instanceof String) {
                    autor.setId(Long.parseLong((String) autorMap.get("id")));
                }
                autor.setNome((String) autorMap.get("nome"));
                autor.setCpfcnpj((String) autorMap.get("cpfcnpj"));
                autor.setTelefone((String) autorMap.get("telefone"));
                autor.setEmail((String) autorMap.get("email"));

                FormAutores formAutores = new FormAutores(autor);
                JDesktopPane desktopPane = getDesktopPane();
                if (desktopPane != null) {
                    desktopPane.add(formAutores);
                    formAutores.setVisible(true);

                    formAutores.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
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
            JOptionPane.showMessageDialog(this, "Selecione um autor para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void excluir() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            if (JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o autor selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Long id = (Long) tabela.getValueAt(selectedRow, 0);
                    apiClient.delete("/autores/" + id);
                    carregar();
                } catch (Exception e) {
                    ExceptionHandler.tratar(e, this);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um autor para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
            String url = "/autores/search?termo=" + java.net.URLEncoder.encode(termo, java.nio.charset.StandardCharsets.UTF_8.toString());
            List<AutorDTO> autores = apiClient.get(url, ApiClient.listOf(AutorDTO.class));
            carregarDados(autores);
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    @Override
    protected void carregar() {
        try {
            List<AutorDTO> autores = apiClient.get("/autores", ApiClient.listOf(AutorDTO.class));
            carregarDados(autores);
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    private void carregarDados(List<AutorDTO> autores) {
        model.setRowCount(0);
        if (autores == null) return;

        for (AutorDTO autor : autores) {
            model.addRow(new Object[]{
                    autor.getId(),
                    autor.getNome(),
                    formatarCpfCnpj(autor.getCpfcnpj()),
                    formatarTelefone(autor.getTelefone()),
                    autor.getEmail()
            });
        }
    }

    private String formatarCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null) return cpfCnpj;
        if (cpfCnpj.length() == 11) {
            return cpfCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        if (cpfCnpj.length() == 14) {
            return cpfCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return cpfCnpj;
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() < 10) return telefone;
        if (telefone.length() == 11) {
            return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        return telefone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }
}
