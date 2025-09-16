package com.biblioteca.presentation.formularios;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.presentation.templates.FormPadrao;

public class FormAutores extends FormPadrao {

    private IAutorService autorService;

    private JTextField txtNome;
    private JTextField txtEmail;
    private JFormattedTextField txtCpfCnpj;
    private JFormattedTextField txtTelefone;

    public FormAutores(IAutorService autorService) {
        super("Formulário de Autores");
        this.autorService = autorService;
    }

    @Override
    protected void configFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados do Autor"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelFormulario.add(new JLabel("Nome:"), gbc);

        gbc.gridy++;
        txtNome = new JTextField(30);
        painelFormulario.add(txtNome, gbc);

        // Linha 1: CPF/CNPJ e Telefone
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        painelFormulario.add(new JLabel("CPF/CNPJ:"), gbc);

        gbc.gridx = 1;
        painelFormulario.add(new JLabel("Telefone:"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        txtCpfCnpj = new JFormattedTextField();
        try {
            MaskFormatter mascaraCpfCnpj = new MaskFormatter("###.###.###-##");
            mascaraCpfCnpj.setPlaceholderCharacter('_');
            mascaraCpfCnpj.install(txtCpfCnpj);
        } catch (ParseException e) {
            // Silencioso, o campo funcionará como um JTextField normal
        }
        painelFormulario.add(txtCpfCnpj, gbc);

        gbc.gridx = 1;
        txtTelefone = new JFormattedTextField();
        try {
            MaskFormatter mascaraTelefone = new MaskFormatter("(##) #####-####");
            mascaraTelefone.setPlaceholderCharacter('_');
            mascaraTelefone.install(txtTelefone);
        } catch (ParseException e) {
            // Silencioso
        }
        painelFormulario.add(txtTelefone, gbc);

        // Linha 2: Email
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        painelFormulario.add(new JLabel("Email:"), gbc);

        gbc.gridy++;
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail, gbc);

        add(painelFormulario);
    }

    @Override
    protected void salvar() {
        try {
            String nome = txtNome.getText();
            String cpfcnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
            String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
            String email = txtEmail.getText();

            if (nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "O campo 'Nome' é obrigatório.", "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cpfcnpj.isBlank()) {
                JOptionPane.showMessageDialog(this, "O campo 'CPF/CNPJ' é obrigatório.", "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (telefone.isBlank()) {
                JOptionPane.showMessageDialog(this, "O campo 'Telefone' é obrigatório.", "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (email.isBlank()) {
                JOptionPane.showMessageDialog(this, "O campo 'Email' é obrigatório.", "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            AutorDTO autorDTO = new AutorDTO(null, nome, cpfcnpj, telefone, email);
            autorService.salvar(autorDTO);

            JOptionPane.showMessageDialog(this, "Autor salvo com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o autor: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
