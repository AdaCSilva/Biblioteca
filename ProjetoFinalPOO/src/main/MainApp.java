package main;

import view.*;
import javax.swing.*;

public class MainApp {

    public static void main(String[] args) {
        // Exibe a tela de login
        LoginDialog loginDialog = new LoginDialog(null);
        loginDialog.setVisible(true);

        // Verifica se o login foi bem-sucedido
        if (loginDialog.isLoginSucesso()) {
            // Se sim, abre a aplicação principal
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            // Caso contrário, encerra o programa
            JOptionPane.showMessageDialog(null, "Aplicação encerrada.", "Encerrando", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}
