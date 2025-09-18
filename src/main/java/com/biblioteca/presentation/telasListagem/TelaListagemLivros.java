package com.biblioteca.presentation.telasListagem;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.Map;

import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.formularios.FormLivros;
import com.biblioteca.presentation.util.ApiClient;
import com.biblioteca.presentation.templates.TelaListagemPadrao;

public class TelaListagemLivros extends TelaListagemPadrao {

    private final ApiClient apiClient;

    public TelaListagemLivros() {
        super("Listagem de Livros");
        this.apiClient = new ApiClient();
        carregar();
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
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = (Long) tabela.getValueAt(selectedRow, 0);
            LivroDTO livroParaEditar = apiClient.get("/livros/" + id, LivroDTO.class);

            FormLivros form = new FormLivros(livroParaEditar);
            JDesktopPane desktopPane = getDesktopPane();
            if (desktopPane != null) {
                desktopPane.add(form);
                form.setVisible(true);
            }

        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    @Override
    protected void importarPorIsbn() {
        String isbn = JOptionPane.showInputDialog(this, "Digite o ISBN do livro:", "Importar por ISBN", JOptionPane.PLAIN_MESSAGE);
        if (isbn != null && !isbn.trim().isEmpty()) {
            try {
                apiClient.post("/livros/isbn/" + isbn.trim(), null, ApiClient.mapOf(String.class, Object.class));
                JOptionPane.showMessageDialog(this, "Livro importado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregar(); // Recarrega a lista
            } catch (Exception e) {
                ExceptionHandler.tratar(e, this);
            }
        }
    }

    @Override
    protected void excluir() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            if (JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o livro selecionado?", "Confirmação",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Long id = (Long) tabela.getValueAt(selectedRow, 0);
                    apiClient.delete("/livros/" + id);
                    carregar(); // Recarrega a lista
                } catch (Exception e) {
                    ExceptionHandler.tratar(e, this);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void carregar() {
        model.setRowCount(0);
        try {
            List<LivroDTO> livros = apiClient.get("/livros", ApiClient.listOf(LivroDTO.class));
            for (LivroDTO livro : livros) {
                // Obter nomes dos autores
                String autoresNomes = "";
                if (livro.getAutoresIds() != null && !livro.getAutoresIds().isEmpty()) {
                    // Buscar os autores pelo ID e obter seus nomes
                    try {
                        List<String> nomesAutores = new java.util.ArrayList<>();
                        for (Long autorId : livro.getAutoresIds()) {
                            // Buscar autor pelo ID
                            try {
                                Map<String, Object> autor = apiClient.get("/autores/" + autorId,
                                        ApiClient.mapOf(String.class, Object.class));
                                if (autor != null && autor.containsKey("nome")) {
                                    nomesAutores.add(autor.get("nome").toString());
                                }
                            } catch (Exception ex) {
                                // Ignora erro e continua com próximo autor
                            }
                        }
                        autoresNomes = String.join(", ", nomesAutores);
                    } catch (Exception ex) {
                        autoresNomes = "Erro ao carregar autores";
                    }
                }

                // Obter nome da editora
                String editoraNome = "";
                if (livro.getEditoraId() != null) {
                    try {
                        Map<String, Object> editora = apiClient.get("/editoras/" + livro.getEditoraId(),
                                ApiClient.mapOf(String.class, Object.class));
                        if (editora != null && editora.containsKey("nome")) {
                            editoraNome = editora.get("nome").toString();
                        }
                    } catch (Exception ex) {
                        editoraNome = "Erro ao carregar editora";
                    }
                }

                model.addRow(new Object[] {
                        livro.getId(),
                        livro.getIsbn(),
                        livro.getTitulo(),
                        autoresNomes.isEmpty() ? "- Sem autores -" : autoresNomes,
                        editoraNome.isEmpty() ? "- Sem editora -" : editoraNome,
                        livro.getDataPublicacao()
                });
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }
}
