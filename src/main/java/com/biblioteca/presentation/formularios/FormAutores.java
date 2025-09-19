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

import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.infrastructure.exceptions.ExceptionHandler;
import com.biblioteca.infrastructure.util.ValidacaoUtil;
import com.biblioteca.presentation.templates.FormPadrao;

/**
 * Formulário para cadastro e edição de autores.
 * <p>
 * Esta classe implementa a interface gráfica para gerenciar os dados de autores,
 * permitindo a criação de novos autores e a edição de autores existentes.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class FormAutores extends FormPadrao {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JFormattedTextField txtCpfCnpj;
    private JFormattedTextField txtTelefone;

    private AutorDTO autorParaEdicao;

    /**
     * Construtor padrão para criar um novo autor.
     * Inicializa o formulário com o título "Formulário de Autores".
     */
    public FormAutores() {
        super("Formulário de Autores");
    }

    /**
     * Construtor para editar um autor existente.
     * Inicializa o formulário com o título "Edição de Autor" e preenche os campos
     * com os dados do autor fornecido.
     *
     * @param autor o DTO contendo os dados do autor a ser editado
     */
    public FormAutores(AutorDTO autor) {
        super("Edição de Autor");
        if (autor != null) {
            this.autorParaEdicao = autor;
            javax.swing.SwingUtilities.invokeLater(() -> {
                preencherCamposParaEdicao();
            });
        }
    }

    /**
     * Configura o layout e os componentes do formulário.
     * Implementa o método abstrato da classe FormPadrao.
     */
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelFormulario.add(new JLabel("Nome:"), gbc);

        gbc.gridy++;
        txtNome = new JTextField(30);
        painelFormulario.add(txtNome, gbc);

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
        }
        painelFormulario.add(txtCpfCnpj, gbc);

        gbc.gridx = 1;
        txtTelefone = new JFormattedTextField();
        try {
            MaskFormatter mascaraTelefone = new MaskFormatter("(##) #####-####");
            mascaraTelefone.setPlaceholderCharacter('_');
            mascaraTelefone.install(txtTelefone);
        } catch (ParseException e) {
        }
        painelFormulario.add(txtTelefone, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        painelFormulario.add(new JLabel("Email:"), gbc);

        gbc.gridy++;
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        painelFormulario.add(new JPanel(), gbc);

        add(painelFormulario, BorderLayout.CENTER);
    }

    /**
     * Preenche os campos do formulário com os dados do autor para edição.
     * <p>
     * Este método formata e exibe os dados do autor nos campos do formulário,
     * aplicando máscaras de formatação para CPF/CNPJ e telefone quando possível.
     * </p>
     */
    private void preencherCamposParaEdicao() {
        if (autorParaEdicao != null) {
            setTitle("Edição de Autor - ID: " + autorParaEdicao.getId());

            txtNome.setText(autorParaEdicao.getNome());

            String cpfcnpj = autorParaEdicao.getCpfcnpj();
            if (cpfcnpj != null && cpfcnpj.length() == 11) {
                try {
                    txtCpfCnpj.setValue(cpfcnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"));
                } catch (Exception e) {
                    txtCpfCnpj.setText(cpfcnpj);
                }
            } else {
                txtCpfCnpj.setText(cpfcnpj);
            }

            String telefone = autorParaEdicao.getTelefone();
            if (telefone != null && telefone.length() >= 10) {
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
        }
    }

    /**
     * Salva os dados do autor no sistema.
     * <p>
     * Este método valida os dados inseridos no formulário, cria ou atualiza
     * o autor no sistema e exibe uma mensagem de sucesso ao usuário.
     * </p>
     */
    @Override
    protected void salvar() {
        try {
            ValidacaoUtil.ValidadorFormulario validador = ValidacaoUtil.validador(this);
            validador.campo(txtNome, "Nome").obrigatorio();
            validador.campo(txtEmail, "Email").email();

            if (!validador.validar()) {
                return;
            }

            String nome = txtNome.getText();
            String cpfcnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
            String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
            String email = txtEmail.getText();

            Long id = autorParaEdicao != null ? autorParaEdicao.getId() : null;
            AutorDTO autorDTO = new AutorDTO(id, nome, cpfcnpj, telefone, email);

            autorDTO.validar();

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
            ExceptionHandler.tratar(e, this);
        }
    }
}
