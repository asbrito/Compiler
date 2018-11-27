/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import java.util.LinkedList;

/**
 *
 * @author adrie
 */
public class SyntacticError {
    private LinkedList expectedToken;
    private int line;
    private Token token;

    public Token getToken() {
        return token;
    }

    public SyntacticError(LinkedList expectedToken, int line, Token token) {
        this.expectedToken = expectedToken;
        this.line = line;
        this.token = token;
    }

    
    public LinkedList getExpectedToken() {
        return expectedToken;
    }

    public int getLine() {
        return line;
    }
        
}
