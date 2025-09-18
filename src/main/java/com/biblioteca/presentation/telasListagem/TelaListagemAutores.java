package com.biblioteca.presentation.telasListagem;

import java.util.List;
import java.util.Map;

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

            // Adicionar listener para atualizar a lista quando o formulário for fechado
            formAutores.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                    carregar(); // Recarrega a lista após inclusão
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

                // Usar TypeReference para Map para receber os dados JSON brutos
                Map<String, Object> autorMap = apiClient.get("/autores/" + id,
                        ApiClient.mapOf(String.class, Object.class));

                // Verificar se os dados foram carregados corretamente
                if (autorMap == null) {
                    throw new Exception("Não foi possível carregar os dados do autor.");
                }

                // Criar o AutorDTO manualmente a partir do Map
                AutorDTO autor = new AutorDTO();

                // Converter o ID para Long se for um número
                if (autorMap.get("id") instanceof Number) {
                    autor.setId(((Number) autorMap.get("id")).longValue());
                } else if (autorMap.get("id") instanceof String) {
                    autor.setId(Long.parseLong((String) autorMap.get("id")));
                }

                // Definir os outros campos
                autor.setNome((String) autorMap.get("nome"));
                autor.setCpfcnpj((String) autorMap.get("cpfcnpj"));
                autor.setTelefone((String) autorMap.get("telefone"));
                autor.setEmail((String) autorMap.get("email"));

                System.out.println("AutorDTO criado manualmente: ID=" + autor.getId() + ", Nome=" + autor.getNome());

                // Criar e exibir o formulário de edição
                FormAutores formAutores = new FormAutores(autor);
                JDesktopPane desktopPane = getDesktopPane();
                if (desktopPane != null) {
                    desktopPane.add(formAutores);
                    formAutores.setVisible(true);

                    // Adicionar listener para atualizar a lista quando o formulário for fechado
                    formAutores.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                        @Override
                        public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                            carregar(); // Recarrega a lista após edição
                        }
                    });
                }
            } catch (Exception e) {
                ExceptionHandler.tratar(e, this);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um autor para editar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(this, "Selecione um autor para excluir.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void carregar() {
        model.setRowCount(0); // Limpa a tabela
        try {
            List<AutorDTO> autores = apiClient.get("/autores", ApiClient.listOf(AutorDTO.class));
            for (AutorDTO autor : autores) {
                model.addRow(new Object[] {
                        autor.getId(),
                        autor.getNome(),
                        formatarCpfCnpj(autor.getCpfcnpj()),
                        formatarTelefone(autor.getTelefone()),
                        autor.getEmail()
                });
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    private String formatarCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null) {
            return cpfCnpj;
        }
        if (cpfCnpj.length() == 11) {
            return cpfCnpj.substring(0, 3) + "." + cpfCnpj.substring(3, 6) + "." + cpfCnpj.substring(6, 9) + "-" + cpfCnpj.substring(9, 11);
        } else if (cpfCnpj.length() == 14) {
            return cpfCnpj.substring(0, 2) + "." + cpfCnpj.substring(2, 5) + "." + cpfCnpj.substring(5, 8) + "/" + cpfCnpj.substring(8, 12) + "-" + cpfCnpj.substring(12, 14);
        }
        return cpfCnpj;
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() < 10) {
            return telefone;
        }
        if (telefone.length() == 11) {
            return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 7) + "-" + telefone.substring(7);
        } else {
            return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 6) + "-" + telefone.substring(6);
        }
    }
}
