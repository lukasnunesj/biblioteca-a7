package com.biblioteca.view;

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

import com.formdev.flatlaf.FlatDarculaLaf;

public class TelaPrincipal {

    private final JFrame frame = new JFrame();

    public TelaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        configTela();
        configMenu();

        // Criar desktop pane para janelas internas
        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(240, 240, 240));
        frame.add(desktopPane, BorderLayout.CENTER);

    }

    private void configTela() {
        frame.setTitle("Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // configFecharEvent();
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

        // Menu Autores
        JMenuItem menuItemAutores = new JMenuItem("Autores");
        menuItemAutores.setMnemonic('A');

        // Menu Editoras
        JMenuItem menuItemEditoras = new JMenuItem("Editoras");
        menuItemEditoras.setMnemonic('E');

        // Sair
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.setMnemonic('S');
        itemSair.addActionListener(e -> sairAplicacao());

        menuInicio.add(itemSair);
        menuInicio.add(menuItemLivros);
        menuInicio.add(menuItemAutores);
        menuInicio.add(menuItemEditoras);

        menuBar.add(menuInicio);
        frame.setJMenuBar(menuBar);
    }

    private void configFecharEvent() {
        frame.addWindowFocusListener(new WindowAdapter() {
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
