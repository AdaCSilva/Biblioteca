package view;

import controller.AutoresController;
import model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AutoresDialog extends JDialog {

    private JTable tabelaAutores;
    private DefaultTableModel modeloTabela;

    public AutoresDialog(JFrame parent, boolean modal) {
        super(parent, "Gerenciar Autores", modal);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Configuração da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Nacionalidade"}, 0);
        tabelaAutores = new JTable(modeloTabela);
        tabelaAutores.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaAutores);
        add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar Autor");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        btnAdicionar.addActionListener(e -> adicionarAutor());
        btnAtualizar.addActionListener(e -> atualizarLista());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        add(painelBotoes, BorderLayout.SOUTH);

        // Atualiza a tabela ao abrir a interface
        atualizarLista();
    }

    private void atualizarLista() {
        modeloTabela.setRowCount(0); // Limpa os dados da tabela
        List<Autor> autores = AutoresController.listarAutores();

        for (Autor autor : autores) {
            modeloTabela.addRow(new Object[]{
                    autor.getId(),
                    autor.getNome(),
                    autor.getNacionalidade()
            });
        }
    }

    private void adicionarAutor() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Autor:");
        if (nome == null || nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nacionalidade = JOptionPane.showInputDialog(this, "Nacionalidade do Autor:");
        if (nacionalidade == null || nacionalidade.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nacionalidade não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Autor novoAutor = new Autor(0, nome, nacionalidade);
        if (AutoresController.adicionarAutor(novoAutor)) {
            JOptionPane.showMessageDialog(this, "Autor adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarLista();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}