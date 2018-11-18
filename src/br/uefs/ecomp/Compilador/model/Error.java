/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

/**
 *
 * @author Adriel Brito & Natália Rosa
 */
public class Error {
    private String type;
    private String lexeme;
    private int line;

    public Error(String lexeme, String type, int line) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getType() {
        return type;
    }

    public int getLine() {
        return line;
    }
    
}
