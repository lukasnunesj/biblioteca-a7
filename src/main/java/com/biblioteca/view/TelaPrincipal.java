package com.biblioteca.view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.formdev.flatlaf.FlatDarculaLaf;

public class TelaPrincipal {

    private JFrame frame;

    public TelaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        configTela();
        configMenu();

    }

    private void configTela() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Biblioteca");
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setSize(new Dimension(1080, 720));
        frame.setMinimumSize(new Dimension(1080, 720));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
    }

    private void configMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Início
        JMenuItem menuInicio = new JMenuItem("Início");
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
