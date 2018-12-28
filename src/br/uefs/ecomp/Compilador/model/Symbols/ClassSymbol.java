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
public class ClassSymbol extends Symbol{
    private LinkedList attributeList;
    private LinkedList methodList;
    private String motherClass;

    public String getMotherClass() {
        return motherClass;
    }

    public void setMotherClass(String motherClass) {
        this.motherClass = motherClass;
    }

    public LinkedList getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(LinkedList attributeList) {
        this.attributeList = attributeList;
    }

    public LinkedList getMethodList() {
        return methodList;
    }

    public void setMethodList(LinkedList methodList) {
        this.methodList = methodList;
    }
   
    @Override
    public boolean equals(Object o){
        ClassSymbol c = (ClassSymbol)o;
        return this.getToken().getLexeme().equals(c.getToken().getLexeme());
    }
}
