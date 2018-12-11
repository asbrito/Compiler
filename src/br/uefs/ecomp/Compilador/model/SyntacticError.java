/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import java.util.LinkedList;

/**
 *
 * @author Adriel Brito e Natália Rosa
 */
public class SyntacticError {
    private final LinkedList expectedToken;
    private final int line;
    private final Token token;

    public Token getToken() {
        return token;
    }

    public SyntacticError(LinkedList expectedToken, int line, Token token) {
        this.expectedToken = expectedToken;
        this.line = line;
        this.token = token;
    }

    public SyntacticError(LinkedList expectedToken, int line) {
        this.expectedToken = expectedToken;
        this.line = line;
        this.token = new Token("End of file exception");
    }
    public SyntacticError(int line) {
        this.expectedToken = new LinkedList ();
        expectedToken.add("");
        this.line = line;
        this.token = new Token("End of file exception");
    }
    
    public LinkedList getExpectedToken() {
        return expectedToken;
    }

    public int getLine() {
        return line;
    }
        
}
