/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model.Symbols;

import java.util.LinkedList;

/**
 *
 * @author adrie
 */
public class MethodSymbol extends Symbol{
    private LinkedList variablesList;
    private LinkedList parameterList;

    public LinkedList getVariablesList() {
        return variablesList;
    }

    public void setVariablesList(LinkedList variablesList) {
        this.variablesList = variablesList;
    }

    public LinkedList getParameterList() {
        return parameterList;
    }

    public void setParameterList(LinkedList parameterList) {
        this.parameterList = parameterList;
    }
    
    
}
