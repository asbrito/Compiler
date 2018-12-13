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
    //lista tokkens
    private LinkedList tableList;

    public SymbolTable(LinkedList tableList) {
        this.tableList = tableList;
    }

    public LinkedList getTableList() {
        return tableList;
    }
    
    
}
