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
public class ConstantSymbol extends Symbol{
    private String value;

    public ConstantSymbol(Object scope, String type, String identifier) {
        super(scope, type, identifier);
        this.value = value;
    }

    public ConstantSymbol() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
