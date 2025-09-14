package com.biblioteca.view;

import javax.swing.JInternalFrame;

public class TelaListagemLivros {
    private final JInternalFrame internalFrame = new JInternalFrame();

    public TelaListagemLivros() {
        internalFrame.setTitle("Listagem de Livros");
        internalFrame.setSize(600, 400);
        internalFrame.setVisible(true);
    }
}
