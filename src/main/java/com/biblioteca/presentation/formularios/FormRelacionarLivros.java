package com.biblioteca.presentation.formularios;

import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.util.ApiClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormRelacionarLivros extends JDialog {

    private JTable tabelaLivrosDisponiveis;
    private JTable tabelaLivrosRelacionados;
    private DefaultTableModel modelDisponiveis;
    private DefaultTableModel modelRelacionados;
    private JButton btnAdicionar, btnRemover, btnSalvar, btnCancelar;
    private JTextField campoPesquisa;

    private ApiClient apiClient;
    private LivroDTO livroPrincipal;
    private List<LivroDTO> todosOsLivros;
    private List<LivroDTO> livrosDisponiveis; // Lista que armazena os livros disponíveis
    private List<LivroDTO> livrosRelacionadosAtualmente;

    public FormRelacionarLivros(Window owner, LivroDTO livroPrincipal) {
        super(owner, ModalityType.APPLICATION_MODAL);
        this.livroPrincipal = livroPrincipal;
        this.apiClient = new ApiClient();
        this.livrosRelacionadosAtualmente = new ArrayList<>();
        this.livrosDisponiveis = new ArrayList<>();

        setTitle("Relacionar Livros Semelhantes para: " + livroPrincipal.getTitulo());
        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        initComponents();
        carregarDados();
    }

    private void initComponents() {
        // Models
        modelDisponiveis = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Título"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelRelacionados = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Título"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Tables
        tabelaLivrosDisponiveis = new JTable(modelDisponiveis);
        tabelaLivrosRelacionados = new JTable(modelRelacionados);
        tabelaLivrosDisponiveis.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabelaLivrosRelacionados.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Search Panel
        campoPesquisa = new JTextField();
        JPanel painelPesquisa = new JPanel(new BorderLayout());
        painelPesquisa.add(new JLabel("Pesquisar: "), BorderLayout.WEST);
        painelPesquisa.add(campoPesquisa, BorderLayout.CENTER);

        JPanel painelTabelaDisponiveis = new JPanel(new BorderLayout(5, 5));
        painelTabelaDisponiveis.setBorder(BorderFactory.createTitledBorder("Livros Disponíveis"));
        painelTabelaDisponiveis.add(painelPesquisa, BorderLayout.NORTH);
        painelTabelaDisponiveis.add(new JScrollPane(tabelaLivrosDisponiveis), BorderLayout.CENTER);

        // Center Buttons Panel
        btnAdicionar = new JButton(">>");
        btnRemover = new JButton("<<");
        JPanel painelBotoesAcao = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        painelBotoesAcao.add(btnAdicionar, gbc);
        gbc.gridy = 1;
        painelBotoesAcao.add(btnRemover, gbc);

        // Related Books Panel
        JPanel painelTabelaRelacionados = new JPanel(new BorderLayout(5, 5));
        painelTabelaRelacionados.setBorder(BorderFactory.createTitledBorder("Livros Relacionados"));
        painelTabelaRelacionados.add(new JScrollPane(tabelaLivrosRelacionados), BorderLayout.CENTER);

        // Main content panel with 3 columns
        JPanel painelPrincipal = new JPanel(new GridLayout(1, 3, 10, 10));
        painelPrincipal.add(painelTabelaDisponiveis);
        painelPrincipal.add(painelBotoesAcao);
        painelPrincipal.add(painelTabelaRelacionados);

        // Bottom buttons panel
        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        painelBotoesInferior.add(btnSalvar);
        painelBotoesInferior.add(btnCancelar);

        // Add listeners
        btnAdicionar.addActionListener(e -> adicionarRelacionamento());
        btnRemover.addActionListener(e -> removerRelacionamento());
        btnSalvar.addActionListener(e -> salvarRelacionamentos());
        btnCancelar.addActionListener(e -> dispose());
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarTabelaDisponiveis(); }
            public void removeUpdate(DocumentEvent e) { filtrarTabelaDisponiveis(); }
            public void changedUpdate(DocumentEvent e) { filtrarTabelaDisponiveis(); }
        });

        // Add panels to dialog
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoesInferior, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        SwingUtilities.invokeLater(() -> {
            try {
                todosOsLivros = apiClient.get("/livros", ApiClient.listOf(LivroDTO.class));
                LivroDTO livroPrincipalAtualizado = apiClient.get("/livros/" + livroPrincipal.getId(), LivroDTO.class);
                List<Long> idsRelacionados = livroPrincipalAtualizado.getLivrosSemelhantesIds();

                livrosDisponiveis.clear();
                livrosRelacionadosAtualmente.clear();

                for (LivroDTO livro : todosOsLivros) {
                    if (livro.getId().equals(livroPrincipal.getId())) continue;

                    if (idsRelacionados.contains(livro.getId())) {
                        livrosRelacionadosAtualmente.add(livro);
                    } else {
                        livrosDisponiveis.add(livro);
                    }
                }

                popularTabela(modelRelacionados, livrosRelacionadosAtualmente);
                filtrarTabelaDisponiveis(); // Initial population of available books table

            } catch (Exception e) {
                ExceptionHandler.tratar(e, (JComponent) getContentPane());
            }
        });
    }

    private void filtrarTabelaDisponiveis() {
        String texto = campoPesquisa.getText().toLowerCase();
        List<LivroDTO> livrosFiltrados = livrosDisponiveis.stream()
                .filter(livro -> livro.getTitulo().toLowerCase().contains(texto))
                .collect(Collectors.toList());
        popularTabela(modelDisponiveis, livrosFiltrados);
    }

    private void popularTabela(DefaultTableModel model, List<LivroDTO> livros) {
        model.setRowCount(0);
        for (LivroDTO livro : livros) {
            model.addRow(new Object[]{livro.getId(), livro.getTitulo()});
        }
    }

    private void adicionarRelacionamento() {
        moverLinhas(tabelaLivrosDisponiveis, modelDisponiveis, livrosDisponiveis, livrosRelacionadosAtualmente);
    }

    private void removerRelacionamento() {
        moverLinhas(tabelaLivrosRelacionados, modelRelacionados, livrosRelacionadosAtualmente, livrosDisponiveis);
    }

    private void moverLinhas(JTable tabelaOrigem, DefaultTableModel modelOrigem, List<LivroDTO> listaOrigem, List<LivroDTO> listaDestino) {
        int[] selectedRows = tabelaOrigem.getSelectedRows();
        if (selectedRows.length == 0) return;

        List<LivroDTO> livrosParaMover = new ArrayList<>();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int modelRowIndex = tabelaOrigem.convertRowIndexToModel(selectedRows[i]);
            long livroId = (long) modelOrigem.getValueAt(modelRowIndex, 0);

            listaOrigem.stream()
                .filter(l -> l.getId().equals(livroId))
                .findFirst()
                .ifPresent(livrosParaMover::add);
        }

        livrosParaMover.forEach(livro -> {
            listaOrigem.remove(livro);
            listaDestino.add(livro);
        });

        filtrarTabelaDisponiveis();
        popularTabela(modelRelacionados, livrosRelacionadosAtualmente);
    }

    private void salvarRelacionamentos() {
        try {
            List<Long> idsFinaisRelacionados = livrosRelacionadosAtualmente.stream()
                    .map(LivroDTO::getId)
                    .collect(Collectors.toList());

            livroPrincipal.setLivrosSemelhantesIds(idsFinaisRelacionados);

            apiClient.put("/livros/" + livroPrincipal.getId(), livroPrincipal, LivroDTO.class);

            JOptionPane.showMessageDialog(this, "Relacionamentos salvos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            ExceptionHandler.tratar(e, (JComponent) getContentPane());
        }
    }
}
