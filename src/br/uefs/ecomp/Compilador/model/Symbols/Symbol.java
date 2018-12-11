/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model.Symbols;

/**
 *
 * @author adrie
 */
public abstract class Symbol {
    private Object scope;
    private String type;
    private String identifier;

    public Symbol(Object scope, String type, String identifier) {
        this.scope = scope;
        this.type = type;
        this.identifier = identifier;
    }

    public Symbol() {
    }

    public Object getScope() {
        return scope;
    }

    public void setScope(Object scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
        
}
