package com.biblioteca.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import com.biblioteca.infrastructure.factory.DependencyFactory;
import com.biblioteca.infrastructure.util.JPAUtil;
import com.biblioteca.presentation.telasListagem.TelaListagemAutores;
import com.biblioteca.presentation.telasListagem.TelaListagemEditoras;
import com.biblioteca.presentation.telasListagem.TelaListagemLivros;
import com.formdev.flatlaf.FlatDarculaLaf;

public class TelaPrincipal {

    private final JFrame frame = new JFrame();
    private JDesktopPane desktopPane;
    private final ILivroService livroService;
    private final IAutorService autorService;
    private final IEditoraService editoraService;

    public TelaPrincipal() {
        DependencyFactory factory = new DependencyFactory();
        this.livroService = factory.createLivroService();
        this.autorService = factory.createAutorService();
        this.editoraService = factory.createEditoraService();
        initComponents();
    }

    private void initComponents() {
        configTela();
        configMenu();

        // Criar desktop pane para janelas internas
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(240, 240, 240));
        frame.add(desktopPane, BorderLayout.CENTER);

    }

    private void configTela() {
        frame.setTitle("Biblioteca");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        configFecharEvent();
        frame.setResizable(false);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setSize(new Dimension(1080, 720));
        frame.setMinimumSize(new Dimension(1080, 720));
        frame.setLayout(new BorderLayout());

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    private void configMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Início
        JMenu menuInicio = new JMenu("Início");
        menuInicio.setMnemonic('I');

        // Menu Livros
        JMenuItem menuItemLivros = new JMenuItem("Livros");
        menuItemLivros.setMnemonic('L');
        menuItemLivros.addActionListener(e -> {
            TelaListagemLivros telaListagemLivros = new TelaListagemLivros(livroService);
            desktopPane.add(telaListagemLivros);
            telaListagemLivros.setVisible(true);
        });

        // Menu Autores
        JMenuItem menuItemAutores = new JMenuItem("Autores");
        menuItemAutores.setMnemonic('A');
        menuItemAutores.addActionListener(e -> {
            TelaListagemAutores telaListagemAutores = new TelaListagemAutores(autorService);
            desktopPane.add(telaListagemAutores);
            telaListagemAutores.setVisible(true);
        });

        // Menu Editoras
        JMenuItem menuItemEditoras = new JMenuItem("Editoras");
        menuItemEditoras.setMnemonic('E');
        menuItemEditoras.addActionListener(e -> {
            TelaListagemEditoras telaListagemEditoras = new TelaListagemEditoras(editoraService);
            desktopPane.add(telaListagemEditoras);
            telaListagemEditoras.setVisible(true);
        });

        // Sair
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.setMnemonic('S');
        itemSair.addActionListener(e -> sairAplicacao());

        menuInicio.add(menuItemLivros);
        menuInicio.add(menuItemAutores);
        menuInicio.add(menuItemEditoras);
        menuInicio.add(itemSair);

        menuBar.add(menuInicio);
        frame.setJMenuBar(menuBar);
    }

    private void configFecharEvent() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sairAplicacao();
            }
        });
    }

    private void sairAplicacao() {
        int opcao = JOptionPane.showConfirmDialog(
                this.frame,
                "Deseja realmente sair da aplicação?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            JPAUtil.close();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Set up the Darcula theme before creating any UI components
        FlatDarculaLaf.setup();

        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal();
        });
    }
}
