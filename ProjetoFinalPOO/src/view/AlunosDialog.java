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
        JButton btnRemover = new JButton("Remover Aluno");
        JButton btnAlterar = new JButton("Alterar Aluno");

        btnAdicionar.addActionListener(e -> adicionarAluno());
        btnAtualizar.addActionListener(e -> atualizarLista());
        btnRemover.addActionListener(e -> removerAluno());
        btnAlterar.addActionListener(e -> alterarAluno());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAlterar);
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
    
    private void removerAluno() {
        int linhaSelecionada = tabelaAlunos.getSelectedRow(); // Obtém a linha selecionada

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o ID do autor da tabela
        int idAluno = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        // Confirmar a remoção
        int confirmacao = JOptionPane.showConfirmDialog(
                this, "Deseja realmente remover este aluno?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (AlunosController.removerAluno(idAluno)) {
                JOptionPane.showMessageDialog(this, "Aluno removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void alterarAluno() {
        int linhaSelecionada = tabelaAlunos.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o ID do livro da tabela
        int idAluno = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        // Obter os dados do livro atual
        String nomeAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        String matriculaAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 2);

        // Criar campos para edição
        JTextField campoNome = new JTextField(nomeAtual);
        JTextField campoMatricula = new JTextField(matriculaAtual);

        Object[] mensagem = {
                "Nome do aluno:", campoNome,
                "Matrícula do aluno:", campoMatricula
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, mensagem, "Alterar Autor", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            String novoNome = campoNome.getText().trim();
            String novaMatricula = campoMatricula.getText().trim();

            if (novoNome.isEmpty() || novaMatricula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Aluno alunoAlterado = new Aluno(idAluno, novoNome, novaMatricula);

            if (AlunosController.atualizarAluno(alunoAlterado)) {
                JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}