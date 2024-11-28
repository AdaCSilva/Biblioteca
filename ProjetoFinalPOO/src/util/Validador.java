package util;

public class Validador {
    public static boolean validarString(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            System.err.println("O campo " + campo + " não pode estar vazio.");
            return false;
        }
        return true;
    }

    public static boolean validarNumero(int valor, String campo) {
        if (valor < 0) {
            System.err.println("O campo " + campo + " deve ser um número positivo.");
            return false;
        }
        return true;
    }
}