package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validador {
    
    private static final SimpleDateFormat FORMATO_USUARIO = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_MYSQL = new SimpleDateFormat("yyyy-MM-dd");
    
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
    
    /**
     * Valida uma string de data no formato de entrada "dd/MM/yyyy".
     */
    public static boolean validarDataUsuario(String data, String campo) {
        try {
            FORMATO_USUARIO.setLenient(false);
            FORMATO_USUARIO.parse(data);
            return true;
        } catch (ParseException e) {
            System.err.println("Erro ao validar data no campo: " + campo);
            return false;
        }
    }

    /**
     * Converte uma data String (dd/MM/yyyy) para o formato Date.
     */
    public static Date converterDataUsuarioParaDate(String data) {
        try {
            return FORMATO_USUARIO.parse(data);
        } catch (ParseException e) {
            System.err.println("Erro ao converter data do usuário: " + data);
            return null;
        }
    }

    /**
     * Converte uma data Date para o formato String utilizado no banco (yyyy-MM-dd).
     */
    public static String converterDataParaMySQL(Date data) {
        return FORMATO_MYSQL.format(data);
    }

    /**
     * Converte uma data Date do banco para o formato dd/MM/yyyy (String).
     */
    public static String converterDataParaUsuario(Date data) {
        return FORMATO_USUARIO.format(data);
    }

}