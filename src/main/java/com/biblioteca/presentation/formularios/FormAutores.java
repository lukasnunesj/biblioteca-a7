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
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.infrastructure.util.ValidacaoUtil;
import com.biblioteca.presentation.templates.FormPadrao;

public class FormAutores extends FormPadrao {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JFormattedTextField txtCpfCnpj;
    private JFormattedTextField txtTelefone;

    private AutorDTO autorParaEdicao;

    public FormAutores() {
        super("Formulário de Autores");
    }

    public FormAutores(AutorDTO autor) {
        super("Edição de Autor");
        if (autor != null) {
            this.autorParaEdicao = autor;
            javax.swing.SwingUtilities.invokeLater(() -> {
                preencherCamposParaEdicao();
            });
        }
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

    /**
     * Preenche os campos do formulário com os dados do autor para edição
     */
    private void preencherCamposParaEdicao() {
        if (autorParaEdicao != null) {
            System.out.println("Preenchendo campos para edição do autor: " + autorParaEdicao.getId() + " - "
                    + autorParaEdicao.getNome());

            // Definir o título do formulário para incluir o ID do autor
            setTitle("Edição de Autor - ID: " + autorParaEdicao.getId());

            txtNome.setText(autorParaEdicao.getNome());

            // Formatar CPF/CNPJ para exibição
            String cpfcnpj = autorParaEdicao.getCpfcnpj();
            if (cpfcnpj != null && cpfcnpj.length() == 11) {
                // Formatar CPF: XXX.XXX.XXX-XX
                try {
                    txtCpfCnpj.setValue(cpfcnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"));
                } catch (Exception e) {
                    txtCpfCnpj.setText(cpfcnpj);
                }
            } else {
                txtCpfCnpj.setText(cpfcnpj);
            }

            // Formatar telefone para exibição
            String telefone = autorParaEdicao.getTelefone();
            if (telefone != null && telefone.length() >= 10) {
                // Formatar telefone: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX
                try {
                    if (telefone.length() == 11) {
                        txtTelefone.setValue(telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3"));
                    } else {
                        txtTelefone.setValue(telefone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3"));
                    }
                } catch (Exception e) {
                    txtTelefone.setText(telefone);
                }
            } else {
                txtTelefone.setText(telefone);
            }

            txtEmail.setText(autorParaEdicao.getEmail());
        } else {
            System.out.println("Erro: autorParaEdicao é null no método preencherCamposParaEdicao");
        }
    }

    @Override
    protected void salvar() {
        try {
            // Validação centralizada dos campos do formulário
            ValidacaoUtil.ValidadorFormulario validador = ValidacaoUtil.validador(this);
            validador.campo(txtNome, "Nome").obrigatorio();
            validador.campo(txtCpfCnpj, "CPF/CNPJ").obrigatorio();
            validador.campo(txtTelefone, "Telefone").obrigatorio();
            validador.campo(txtEmail, "Email").obrigatorio().email();

            if (!validador.validar()) {
                return;
            }

            // Processamento dos dados após validação
            String nome = txtNome.getText();
            String cpfcnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
            String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
            String email = txtEmail.getText();

            // Cria o DTO e valida os dados de negócio
            Long id = autorParaEdicao != null ? autorParaEdicao.getId() : null;
            AutorDTO autorDTO = new AutorDTO(id, nome, cpfcnpj, telefone, email);

            // A validação de negócio é feita pelo próprio DTO
            autorDTO.validar();

            // Envia os dados para a API (POST para criar, PUT para atualizar)
            if (autorParaEdicao == null) {
                apiClient.post("/autores", autorDTO, AutorDTO.class);
                JOptionPane.showMessageDialog(this, "Autor salvo com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                apiClient.put("/autores/" + autorParaEdicao.getId(), autorDTO, AutorDTO.class);
                JOptionPane.showMessageDialog(this, "Autor atualizado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();

        } catch (Exception e) {
            // Tratamento centralizado de exceções
            ExceptionHandler.tratar(e, this);
        }
    }
}
