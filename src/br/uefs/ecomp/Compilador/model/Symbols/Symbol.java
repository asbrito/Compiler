/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model.Symbols;

import br.uefs.ecomp.Compilador.model.Token;

/**
 *
 * @author adrie
 */
public class Symbol {
    private String scope;
    private String type;
    private Token token;
    
    public Symbol(String scope, String type, Token token) {
        this.scope = scope;
        this.type = type;
        this.token = token;
    }

    public Symbol(String type) {
        this.type = type;
    }

    public Symbol() {
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
