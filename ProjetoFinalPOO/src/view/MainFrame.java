package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Biblioteca");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel de botões
        JPanel painel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnGerenciarAutores = new JButton("Gerenciar Autores");
        JButton btnGerenciarLivros = new JButton("Gerenciar Livros");
        JButton btnGerenciarAlunos = new JButton("Gerenciar Alunos");
        JButton btnGerenciarEmprestimos = new JButton("Gerenciar Empréstimos");
        JButton btnSair = new JButton("Sair");
        
        btnGerenciarAutores.addActionListener(e -> new AutoresDialog(this, true).setVisible(true));
        btnGerenciarLivros.addActionListener(e -> new LivrosDialog(this, true).setVisible(true));
        btnGerenciarAlunos.addActionListener(e -> new AlunosDialog(this, true).setVisible(true));
        btnGerenciarEmprestimos.addActionListener(e -> new EmprestimosDialog(this, true).setVisible(true));
        btnSair.addActionListener(e -> System.exit(0));

        painel.add(btnGerenciarAutores);
        painel.add(btnGerenciarLivros);
        painel.add(btnGerenciarAlunos);
        painel.add(btnGerenciarEmprestimos);
        painel.add(btnSair);

        add(painel, BorderLayout.CENTER);
    }
}
