package view;

import controller.AlunosController;
import controller.EmprestimosController;
import controller.LivrosController;
import model.Aluno;
import model.Emprestimo;
import model.Livro;
import util.Validador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmprestimosDialog extends JDialog {

    private JTable tabelaEmprestimos;
    private DefaultTableModel modeloTabela;

    public EmprestimosDialog(JFrame parent, boolean modal) {
        super(parent, "Gerenciar Empréstimos", modal);
        setSize(700, 500);
        setLocationRelativeTo(parent);

        // Configuração da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Livro", "Aluno", "Data Empréstimo", "Data Devolução"}, 0);
        tabelaEmprestimos = new JTable(modeloTabela);
        tabelaEmprestimos.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaEmprestimos);
        add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar Empréstimo");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        JButton btnRemover = new JButton("Remover Empréstimo");
        JButton btnAlterar = new JButton("Alterar Empréstimo");

        btnAdicionar.addActionListener(e -> adicionarEmprestimo());
        btnAtualizar.addActionListener(e -> atualizarLista());
        btnRemover.addActionListener(e -> removerEmprestimo());
        btnAlterar.addActionListener(e -> alterarEmprestimo());

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
        List<Emprestimo> emprestimos = EmprestimosController.listarEmprestimos();

        for (Emprestimo emprestimo : emprestimos) {
            modeloTabela.addRow(new Object[]{
                emprestimo.getId(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getAluno().getNome(),
                Validador.converterDataParaUsuario(emprestimo.getDataEmprestimo()),
                Validador.converterDataParaUsuario(emprestimo.getDataDevolucao())
            });
        }
    }

    private void adicionarEmprestimo() {
        JComboBox<Livro> comboLivros = new JComboBox<>();
        JComboBox<Aluno> comboAlunos = new JComboBox<>();
        JTextField campoDataEmprestimo = new JTextField("dd/MM/yyyy");
        JTextField campoDataDevolucao = new JTextField("dd/MM/yyyy");

        carregarLivrosNoComboBox(comboLivros);
        carregarAlunosNoComboBox(comboAlunos);

        Object[] mensagem = {
                "Selecione o Livro:", comboLivros,
                "Selecione o Aluno:", comboAlunos,
                "Data de Empréstimo:", campoDataEmprestimo,
                "Data de Devolução:", campoDataDevolucao
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, mensagem, "Adicionar Empréstimo", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            Livro livroSelecionado = (Livro) comboLivros.getSelectedItem();
            Aluno alunoSelecionado = (Aluno) comboAlunos.getSelectedItem();
            String dataEmprestimo = campoDataEmprestimo.getText().trim();
            String dataDevolucao = campoDataDevolucao.getText().trim();

            if (livroSelecionado == null || alunoSelecionado == null ||
                !Validador.validarDataUsuario(dataEmprestimo, "Data de Empréstimo") ||
                !Validador.validarDataUsuario(dataDevolucao, "Data de Devolução")) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Emprestimo novoEmprestimo = new Emprestimo(
                0,
                livroSelecionado,
                alunoSelecionado,
                Validador.converterDataUsuarioParaDate(dataEmprestimo),
                Validador.converterDataUsuarioParaDate(dataDevolucao)
            );

            if (EmprestimosController.adicionarEmprestimo(novoEmprestimo)) {
                JOptionPane.showMessageDialog(this, "Empréstimo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar empréstimo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerEmprestimo() {
        int linhaSelecionada = tabelaEmprestimos.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idEmprestimo = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        int confirmacao = JOptionPane.showConfirmDialog(
                this, "Deseja realmente remover este empréstimo?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (EmprestimosController.removerEmprestimo(idEmprestimo)) {
                JOptionPane.showMessageDialog(this, "Empréstimo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover empréstimo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alterarEmprestimo() {
        int linhaSelecionada = tabelaEmprestimos.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idEmprestimo = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        Emprestimo emprestimoAtual = EmprestimosController.buscarPorId(idEmprestimo);

        JComboBox<Livro> comboLivros = new JComboBox<>();
        JComboBox<Aluno> comboAlunos = new JComboBox<>();
        JTextField campoDataEmprestimo = new JTextField(Validador.converterDataParaUsuario(emprestimoAtual.getDataEmprestimo()));
        JTextField campoDataDevolucao = new JTextField(Validador.converterDataParaUsuario(emprestimoAtual.getDataDevolucao()));

        carregarLivrosNoComboBox(comboLivros);
        carregarAlunosNoComboBox(comboAlunos);

        comboLivros.setSelectedItem(emprestimoAtual.getLivro());
        comboAlunos.setSelectedItem(emprestimoAtual.getAluno());

        Object[] mensagem = {
                "Selecione o Livro:", comboLivros,
                "Selecione o Aluno:", comboAlunos,
                "Data de Empréstimo:", campoDataEmprestimo,
                "Data de Devolução:", campoDataDevolucao
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, mensagem, "Alterar Empréstimo", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            Livro novoLivro = (Livro) comboLivros.getSelectedItem();
            Aluno novoAluno = (Aluno) comboAlunos.getSelectedItem();
            String novaDataEmprestimo = campoDataEmprestimo.getText().trim();
            String novaDataDevolucao = campoDataDevolucao.getText().trim();

            if (novoLivro == null || novoAluno == null ||
                !Validador.validarDataUsuario(novaDataEmprestimo, "Data de Empréstimo") ||
                !Validador.validarDataUsuario(novaDataDevolucao, "Data de Devolução")) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            emprestimoAtual.setLivro(novoLivro);
            emprestimoAtual.setAluno(novoAluno);
            emprestimoAtual.setDataEmprestimo(Validador.converterDataUsuarioParaDate(novaDataEmprestimo));
            emprestimoAtual.setDataDevolucao(Validador.converterDataUsuarioParaDate(novaDataDevolucao));

            if (EmprestimosController.atualizarEmprestimo(emprestimoAtual)) {
                JOptionPane.showMessageDialog(this, "Empréstimo alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar empréstimo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarLivrosNoComboBox(JComboBox<Livro> comboBox) {
        List<Livro> livros = LivrosController.listarLivros();
        comboBox.removeAllItems();

        for (Livro livro : livros) {
            comboBox.addItem(livro);
        }
    }

    private void carregarAlunosNoComboBox(JComboBox<Aluno> comboBox) {
        List<Aluno> alunos = AlunosController.listarAlunos();
        comboBox.removeAllItems();

        for (Aluno aluno : alunos) {
            comboBox.addItem(aluno);
        }
    }
}
