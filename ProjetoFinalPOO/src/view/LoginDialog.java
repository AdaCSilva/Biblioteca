package view;

import controller.UsuarioController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private boolean loginSucesso = false;

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);

        // Painel de entrada
        JPanel painelEntrada = new JPanel(new GridLayout(2, 2, 10, 10));
        painelEntrada.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        painelEntrada.add(txtUsuario);

        painelEntrada.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelEntrada.add(txtSenha);
        add(painelEntrada, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Login");
        JButton btnCancelar = new JButton("Cancelar");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void autenticarUsuario() {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        if (UsuarioController.autenticar(usuario, senha)) {
            loginSucesso = true;
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isLoginSucesso() {
        return loginSucesso;
    }
}