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
public class SymbolTable {
    private LinkedList constList;
    private LinkedList classList;

    public LinkedList getConstList() {
        return constList;
    }

    public void setConstList(LinkedList constList) {
        this.constList = constList;
    }

    public LinkedList getClassList() {
        return classList;
    }

    public void setClassList(LinkedList classList) {
        this.classList = classList;
    }

    public SymbolTable(LinkedList constList, LinkedList classList) {
        this.constList = constList;
        this.classList = classList;
    }  
}
