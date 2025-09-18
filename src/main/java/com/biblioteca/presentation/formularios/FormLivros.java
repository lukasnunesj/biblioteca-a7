package com.biblioteca.presentation.formularios;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.templates.FormPadrao;
import com.biblioteca.presentation.util.ApiClient;

public class FormLivros extends FormPadrao {
    private JTextField txtTitulo, txtIsbn;
    private JFormattedTextField txtDataPublicacao;
    private JComboBox<AutorDTO> cbAutores;
    private JComboBox<EditoraDTO> cbEditora;
    private JButton btnBuscarPorIsbn;
    private JButton btnGerenciarSemelhantes;
    private LivroDTO livroParaEdicao;

    public FormLivros() {
        super("Formulário de Livros");
        btnGerenciarSemelhantes.setEnabled(false); // Desabilita para novos livros
    }

    public FormLivros(LivroDTO livro) {
        super("Edição de Livro");
        this.livroParaEdicao = livro;
        SwingUtilities.invokeLater(this::preencherCamposParaEdicao);
    }

    @Override
    protected void salvar() {
        try {
            Integer dataPublicacao = Integer.parseInt(txtDataPublicacao.getText());

            EditoraDTO editoraSelecionada = (EditoraDTO) cbEditora.getSelectedItem();
            AutorDTO autorSelecionado = (AutorDTO) cbAutores.getSelectedItem();

            if (editoraSelecionada == null || autorSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma editora e um autor.", "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Long> autoresIds = new ArrayList<>();
            autoresIds.add(autorSelecionado.getId());

            Long id = (livroParaEdicao != null) ? livroParaEdicao.getId() : null;
            List<Long> semelhantesIds = (livroParaEdicao != null) ? livroParaEdicao.getLivrosSemelhantesIds()
                    : new ArrayList<>();

            LivroDTO livroDTO = new LivroDTO(
                    id,
                    txtTitulo.getText(),
                    txtIsbn.getText(),
                    dataPublicacao,
                    editoraSelecionada.getId(),
                    autoresIds,
                    semelhantesIds);

            livroDTO.validar();

            if (id == null) {
                // Criação
                apiClient.post("/livros", livroDTO, LivroDTO.class);
                JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Atualização
                apiClient.put("/livros/" + id, livroDTO, LivroDTO.class);
                JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }

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
        painelFormulario.add(new JLabel("Ano Publicação:"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        txtIsbn = new JTextField(13);
        painelFormulario.add(txtIsbn, gbc);

        gbc.gridx = 1;
        try {
            txtDataPublicacao = new JFormattedTextField(new MaskFormatter("####"));
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

        // Adiciona um painel espaçador para empurrar o conteúdo para cima
        gbc.gridy++;
        gbc.weighty = 1.0;
        painelFormulario.add(new JPanel(), gbc);

        add(new JScrollPane(painelFormulario), BorderLayout.CENTER);
    }

    @Override
    protected void configPainelBotoes() {
        super.configPainelBotoes();
        btnGerenciarSemelhantes = new JButton("Gerenciar Semelhantes");
        btnGerenciarSemelhantes.addActionListener(e -> abrirTelaRelacionamento());
        painelBotoes.add(btnGerenciarSemelhantes, 0); // Adiciona o botão no início do painel
    }

    private void abrirTelaRelacionamento() {
        if (livroParaEdicao == null) {
            JOptionPane.showMessageDialog(this, "Você precisa salvar o livro antes de gerenciar seus semelhantes.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FormRelacionarLivros form = new FormRelacionarLivros(SwingUtilities.getWindowAncestor(this), livroParaEdicao);
        form.setVisible(true);
    }

    private void preencherCamposParaEdicao() {
        if (livroParaEdicao == null)
            return;

        setTitle("Edição de Livro - ID: " + livroParaEdicao.getId());
        txtTitulo.setText(livroParaEdicao.getTitulo());
        txtIsbn.setText(livroParaEdicao.getIsbn());

        if (livroParaEdicao.getDataPublicacao() != null) {
            txtDataPublicacao.setText(String.valueOf(livroParaEdicao.getDataPublicacao()));
        }

        // Seleciona a editora no ComboBox
        if (livroParaEdicao.getEditoraId() != null) {
            for (int i = 0; i < cbEditora.getItemCount(); i++) {
                if (cbEditora.getItemAt(i).getId().equals(livroParaEdicao.getEditoraId())) {
                    cbEditora.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Seleciona o primeiro autor no ComboBox
        if (livroParaEdicao.getAutoresIds() != null && !livroParaEdicao.getAutoresIds().isEmpty()) {
            Long primeiroAutorId = livroParaEdicao.getAutoresIds().get(0);
            for (int i = 0; i < cbAutores.getItemCount(); i++) {
                if (cbAutores.getItemAt(i).getId().equals(primeiroAutorId)) {
                    cbAutores.setSelectedIndex(i);
                    break;
                }
            }
        }
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
            List<AutorDTO> autores = apiClient.get("/autores", ApiClient.listOf(AutorDTO.class));
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
            List<EditoraDTO> editoras = apiClient.get("/editoras", ApiClient.listOf(EditoraDTO.class));
            cbEditora.removeAllItems();
            for (EditoraDTO editora : editoras) {
                cbEditora.addItem(editora);
            }
        } catch (Exception e) {
            ExceptionHandler.tratar(e, this);
        }
    }
}
