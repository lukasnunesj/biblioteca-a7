package com.biblioteca.presentation.formularios;

import java.awt.BorderLayout;
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
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.infrastructure.util.ValidacaoUtil;
import com.biblioteca.presentation.templates.FormPadrao;
import com.biblioteca.presentation.util.ApiClient;

public class FormEditoras extends FormPadrao {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JFormattedTextField txtCnpj;
    private JFormattedTextField txtTelefone;

    private EditoraDTO editoraParaEdicao;

    public FormEditoras() {
        super("Formulário de Editoras");
    }

    public FormEditoras(EditoraDTO editora) {
        super("Edição de Editora");
        if (editora != null) {
            this.editoraParaEdicao = editora;
            javax.swing.SwingUtilities.invokeLater(() -> {
                preencherCamposParaEdicao();
            });
        }
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

        // Adiciona um painel espaçador para empurrar o conteúdo para cima
        gbc.gridy++;
        gbc.weighty = 1.0;
        painelFormulario.add(new JPanel(), gbc);

        add(painelFormulario, BorderLayout.CENTER);

    }

    /**
     * Preenche os campos do formulário com os dados da editora para edição
     */
    private void preencherCamposParaEdicao() {
        if (editoraParaEdicao != null) {
            txtNome.setText(editoraParaEdicao.getNome());

            // Formatar CNPJ para exibição
            String cnpj = editoraParaEdicao.getCnpj();
            if (cnpj != null && cnpj.length() == 14) {
                // Formatar CNPJ: XX.XXX.XXX/XXXX-XX
                try {
                    txtCnpj.setValue(cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5"));
                } catch (Exception e) {
                    txtCnpj.setText(cnpj);
                }
            } else {
                txtCnpj.setText(cnpj);
            }

            // Formatar telefone para exibição
            String telefone = editoraParaEdicao.getTelefone();
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

            txtEmail.setText(editoraParaEdicao.getEmail());
        }
    }

    @Override
    protected void salvar() {
        try {
            // Validação centralizada dos campos do formulário
            ValidacaoUtil.ValidadorFormulario validador = ValidacaoUtil.validador(this);
            validador.campo(txtNome, "Nome").obrigatorio();
            validador.campo(txtEmail, "Email").email();

            if (!validador.validar()) {
                return;
            }

            // Processamento dos dados após validação
            String nome = txtNome.getText();
            String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
            String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
            String email = txtEmail.getText();

            // Cria o DTO e valida os dados de negócio
            Long id = editoraParaEdicao != null ? editoraParaEdicao.getId() : null;
            EditoraDTO editoraDTO = new EditoraDTO(id, nome, cnpj, telefone, email);

            // A validação de negócio é feita pelo próprio DTO
            editoraDTO.validar();

            // Envia os dados para a API (POST para criar, PUT para atualizar)
            if (editoraParaEdicao == null) {
                apiClient.post("/editoras", editoraDTO, EditoraDTO.class);
                JOptionPane.showMessageDialog(this, "Editora salva com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                apiClient.put("/editoras/" + editoraParaEdicao.getId(), editoraDTO, EditoraDTO.class);
                JOptionPane.showMessageDialog(this, "Editora atualizada com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();

        } catch (Exception e) {
            // Tratamento centralizado de exceções
            ExceptionHandler.tratar(e, this);
        }
    }
}
