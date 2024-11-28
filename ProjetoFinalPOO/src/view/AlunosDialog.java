package view;

import controller.AlunosController;
import model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AlunosDialog extends JDialog {

    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;

    public AlunosDialog(JFrame parent, boolean modal) {
        super(parent, "Gerenciar Alunos", modal);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Configuração da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Matrícula"}, 0);
        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar Aluno");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        btnAdicionar.addActionListener(e -> adicionarAluno());
        btnAtualizar.addActionListener(e -> atualizarLista());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        add(painelBotoes, BorderLayout.SOUTH);

        // Atualiza a tabela ao abrir a interface
        atualizarLista();
    }

    private void atualizarLista() {
        modeloTabela.setRowCount(0); // Limpa os dados da tabela
        List<Aluno> alunos = AlunosController.listarAlunos();

        for (Aluno aluno : alunos) {
            modeloTabela.addRow(new Object[]{
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getMatricula()
            });
        }
    }

    private void adicionarAluno() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Aluno:");
        if (nome == null || nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String matricula = JOptionPane.showInputDialog(this, "Matrícula do Aluno:");
        if (matricula == null || matricula.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Matrícula não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Aluno novoAluno = new Aluno(0, nome, matricula);
        if (AlunosController.adicionarAluno(novoAluno)) {
            JOptionPane.showMessageDialog(this, "Aluno adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarLista();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}