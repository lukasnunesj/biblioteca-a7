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

import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.infrastructure.util.ValidacaoUtil;
import com.biblioteca.presentation.templates.FormPadrao;
import com.biblioteca.presentation.util.ApiClient;

public class FormEditoras extends FormPadrao {

    private ApiClient apiClient;

    private JTextField txtNome;
    private JTextField txtEmail;
    private JFormattedTextField txtCnpj;
    private JFormattedTextField txtTelefone;

    public FormEditoras() {
        super("Formulário de Editoras");
        this.apiClient = new ApiClient();
    }

    @Override
    protected void configFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados da Editora"),
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

        // Linha 1: CNPJ e Telefone
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        painelFormulario.add(new JLabel("CNPJ:"), gbc);

        gbc.gridx = 1;
        painelFormulario.add(new JLabel("Telefone:"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        txtCnpj = new JFormattedTextField();
        try {
            MaskFormatter mascaraCnpj = new MaskFormatter("##.###.###/####-##");
            mascaraCnpj.setPlaceholderCharacter('_');
            mascaraCnpj.install(txtCnpj);
        } catch (ParseException e) {
            // Silencioso, o campo funcionará como um JTextField normal
        }
        painelFormulario.add(txtCnpj, gbc);

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
            // Validação centralizada dos campos do formulário
            ValidacaoUtil.ValidadorFormulario validador = ValidacaoUtil.validador(this);
            validador.campo(txtNome, "Nome").obrigatorio();
            validador.campo(txtCnpj, "CNPJ").obrigatorio();
            validador.campo(txtTelefone, "Telefone").obrigatorio();
            validador.campo(txtEmail, "Email").obrigatorio().email();
            
            if (!validador.validar()) {
                return;
            }
            
            // Processamento dos dados após validação
            String nome = txtNome.getText();
            String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
            String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
            String email = txtEmail.getText();

            // Cria o DTO e valida os dados de negócio
            EditoraDTO editoraDTO = new EditoraDTO(null, nome, cnpj, telefone, email);
            
            // A validação de negócio é feita pelo próprio DTO
            editoraDTO.validar();
            
            // Envia os dados para a API
            apiClient.post("/editoras", editoraDTO, EditoraDTO.class);

            JOptionPane.showMessageDialog(this, "Editora salva com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            // Tratamento centralizado de exceções
            ExceptionHandler.tratar(e, this);
        }
    }
}
