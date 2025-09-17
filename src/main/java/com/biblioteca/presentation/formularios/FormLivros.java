package com.biblioteca.presentation.formularios;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.util.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import com.biblioteca.presentation.templates.FormPadrao;

public class FormLivros extends FormPadrao {
        private JTextField txtTitulo, txtIsbn;
    private JFormattedTextField txtDataPublicacao;
    private JComboBox<AutorDTO> cbAutores;
    private JComboBox<EditoraDTO> cbEditora;
    private JTable tabelaLivrosSemelhantes;
    private DefaultTableModel modelLivrosSemelhantes;
    private JButton btnRelacionar;
    private JButton btnBuscarPorIsbn;
    private ApiClient apiClient;

    public FormLivros() {
        super("Formulário de Livros");
        this.apiClient = new ApiClient();
    }

    @Override
    protected void initComponents() {
        configTela();
        configPainelBotoes();
        configFormulario();
        configurarEventos();
    }

    @Override
    protected void salvar() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataPublicacao = LocalDate.parse(txtDataPublicacao.getText(), formatter);

            EditoraDTO editoraSelecionada = (EditoraDTO) cbEditora.getSelectedItem();
            AutorDTO autorSelecionado = (AutorDTO) cbAutores.getSelectedItem();

            if (editoraSelecionada == null || autorSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma editora e um autor.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Long> autoresIds = new ArrayList<>();
            autoresIds.add(autorSelecionado.getId());

            LivroDTO livroDTO = new LivroDTO(
                    null, // ID é gerado no backend
                    txtTitulo.getText(),
                    txtIsbn.getText(),
                    dataPublicacao,
                    editoraSelecionada.getId(),
                    autoresIds
            );

            livroDTO.validar();

            apiClient.post("/livros", livroDTO, LivroDTO.class);

            JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    @Override
    protected void configFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados do Livro"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 10, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelFormulario.add(new JLabel("Título:"), gbc);

        gbc.gridy++;
        txtTitulo = new JTextField();
        painelFormulario.add(txtTitulo, gbc);

        // Linha 1: ISBN e Data de Publicação
        gbc.gridy++;
        gbc.gridwidth = 1;
        painelFormulario.add(new JLabel("ISBN:"), gbc);

        gbc.gridx = 1;
        painelFormulario.add(new JLabel("Data de Publicação:"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        txtIsbn = new JTextField(13);
        painelFormulario.add(txtIsbn, gbc);

        gbc.gridx = 1;
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtDataPublicacao = new JFormattedTextField(mascaraData);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            txtDataPublicacao = new JFormattedTextField(); // Fallback
        }
        painelFormulario.add(txtDataPublicacao, gbc);

        // Linha 2: Editora
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        painelFormulario.add(new JLabel("Editora:"), gbc);

        gbc.gridy++;
        cbEditora = new JComboBox<>();
        carregarEditoras();
        cbEditora.setEditable(true);
        painelFormulario.add(cbEditora, gbc);

        // Linha 3: Autores
        gbc.gridy++;
        painelFormulario.add(new JLabel("Autores:"), gbc);

        gbc.gridy++;
        cbAutores = new JComboBox<>();
        carregarAutores();
        cbAutores.setEditable(true);
        painelFormulario.add(cbAutores, gbc);

        // --- Painel de Livros Semelhantes ---
        JPanel painelLivrosSemelhantes = new JPanel(new BorderLayout(10, 10));
        painelLivrosSemelhantes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Livros Semelhantes"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Tabela para listar livros relacionados
        String[] colunas = { "ID", "Título" };
        modelLivrosSemelhantes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaLivrosSemelhantes = new JTable(modelLivrosSemelhantes);
        painelLivrosSemelhantes.add(new JScrollPane(tabelaLivrosSemelhantes), BorderLayout.CENTER);

        // Painel com botão para adicionar relação
        JPanel painelBotoesRelacionar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRelacionar = new JButton("Relacionar");
        btnRelacionar.addActionListener(e -> abrirModalRelacionarLivros());
        painelBotoesRelacionar.add(btnRelacionar);
        painelLivrosSemelhantes.add(painelBotoesRelacionar, BorderLayout.SOUTH);

        // --- Split Pane ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(painelFormulario),
                painelLivrosSemelhantes);
        splitPane.setResizeWeight(0.6); // 60% do espaço para o formulário

        add(splitPane, BorderLayout.CENTER);
    }

    private void abrirModalRelacionarLivros() {
        JOptionPane.showMessageDialog(this,
                "Modal para busca e seleção de livros será implementado aqui.",
                "Relacionar Livros",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarPorIsbn() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um ISBN para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: Implementar busca na API OpenLibrary
        JOptionPane.showMessageDialog(this, "Funcionalidade de busca por ISBN será implementada na próxima fase.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void carregarAutores() {
        try {
            List<AutorDTO> autores = apiClient.get("/autores", new TypeReference<List<AutorDTO>>() {});
            cbAutores.removeAllItems();
            for (AutorDTO autor : autores) {
                cbAutores.addItem(autor);
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }

    private void carregarEditoras() {
        try {
            List<EditoraDTO> editoras = apiClient.get("/editoras", new TypeReference<List<EditoraDTO>>() {});
            cbEditora.removeAllItems();
            for (EditoraDTO editora : editoras) {
                cbEditora.addItem(editora);
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }
}
