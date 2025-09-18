package com.biblioteca.presentation.telasAcoes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.presentation.util.ApiClient;

public class TelaImportacaoLivros extends JInternalFrame {

    private JTextField txtCaminhoArquivo;
    private JButton btnSelecionarArquivo;
    private JButton btnImportar;
    private JButton btnFechar;
    private JButton btnBaixarExemplo;
    private File arquivoSelecionado;
    private final ApiClient apiClient;

    public TelaImportacaoLivros() {
        super("Importar Livros de CSV", true, true, true, true);
        this.apiClient = new ApiClient();
        initComponents();
    }

    private void initComponents() {
        configTela();
        configComponentes();
        configEventos();
    }

    private void configTela() {
        setSize(500, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void configComponentes() {
        JPanel painelSuperior = new JPanel(new BorderLayout(5, 5));
        txtCaminhoArquivo = new JTextField();
        txtCaminhoArquivo.setEditable(false);
        btnSelecionarArquivo = new JButton("Selecionar Arquivo");

        painelSuperior.add(new JLabel("Arquivo:"), BorderLayout.WEST);
        painelSuperior.add(txtCaminhoArquivo, BorderLayout.CENTER);
        painelSuperior.add(btnSelecionarArquivo, BorderLayout.EAST);

        add(painelSuperior, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBaixarExemplo = new JButton("Baixar Exemplo");
        btnImportar = new JButton("Importar");
        btnFechar = new JButton("Fechar");

        btnImportar.setEnabled(false);

        painelBotoes.add(btnBaixarExemplo);
        painelBotoes.add(btnImportar);
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configEventos() {
        btnFechar.addActionListener(e -> dispose());

        btnSelecionarArquivo.addActionListener(e -> selecionarArquivo());

        btnImportar.addActionListener(e -> importarArquivo());

        btnBaixarExemplo.addActionListener(e -> baixarExemplo());
    }

    private void selecionarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione um arquivo CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            arquivoSelecionado = fileChooser.getSelectedFile();
            txtCaminhoArquivo.setText(arquivoSelecionado.getAbsolutePath());
            btnImportar.setEnabled(true);
        }
    }

    private void importarArquivo() {
        if (arquivoSelecionado != null) {
            try {
                apiClient.postMultipart("/livros/importar-csv", "file", arquivoSelecionado);
                JOptionPane.showMessageDialog(this, "Arquivo enviado para importação!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                ExceptionHandler.tratar(e, this);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void baixarExemplo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar arquivo de exemplo");
        fileChooser.setSelectedFile(new File("Exemplo.csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.append("titulo,isbn,data_publicacao,editora,autores\n");
                writer.append("O Senhor dos Anéis,978-8595084759,1954-07-29,HarperCollins,J.R.R. Tolkien\n");
                JOptionPane.showMessageDialog(this, "Arquivo de exemplo salvo com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                ExceptionHandler.tratar(e, this);
            }
        }
    }
}
