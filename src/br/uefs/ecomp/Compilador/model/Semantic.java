/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import br.uefs.ecomp.Compilador.model.Symbols.ConstSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.SymbolTable;

/**
 *
 * @author Adriel Brito e Natália Rosa
 */
public class Semantic {
    private SymbolTable symbolTable;
    int i = 0;
    
    private void typeCheckConst(){
        if ((symbolTable.getTableList().get(i)) instanceof ConstSymbol){
            
            while(!((ConstSymbol) symbolTable.getTableList().get(i)).getConstList().isEmpty()){
                
            }
        }
    }
}