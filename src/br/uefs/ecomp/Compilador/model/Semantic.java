/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import br.uefs.ecomp.Compilador.model.Symbols.ClassSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.ConstSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.ConstantSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.SymbolTable;

/**
 *
 * @author Adriel Brito e Natália Rosa
 */
public class Semantic {

    private SymbolTable symbolTable;
    int i = 0;
    
    private void semanticController (){
        if ((symbolTable.getTableList().get(i)) instanceof ConstSymbol) {
            typeCheckConst();
        }
        else if ((symbolTable.getTableList().get(i)) instanceof ClassSymbol) {
            checkClass();
        }
    }

    private void typeCheckConst() {
        if ((symbolTable.getTableList().get(i)) instanceof ConstSymbol) {
            int j = 0;
            while (((ConstSymbol) symbolTable.getTableList().get(i)).getConstList().size() > j) {
                switch (((ConstantSymbol) (((ConstSymbol) symbolTable.getTableList().get(i)).getConstList()).get(j)).getType()) {
                    case "int": {
                        String value = ((ConstantSymbol) (((ConstSymbol) symbolTable.getTableList().get(i)).getConstList()).get(j)).getValue();
                        try {
                            int valueInt = Integer.parseInt(value);
                        } catch (java.lang.NumberFormatException ex) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    case "float": {
                        String value = ((ConstantSymbol) (((ConstSymbol) symbolTable.getTableList().get(i)).getConstList()).get(j)).getValue();
                        if (!(value.contains("."))) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    case "bool": {
                        String value = ((ConstantSymbol) (((ConstSymbol) symbolTable.getTableList().get(i)).getConstList()).get(j)).getValue();
                        if (!(value.equals("true") || value.equals("false"))) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    case "void":
                        System.out.println("Void não pode.");
                        break;
                    case "string": {
                        String value = ((ConstantSymbol) (((ConstSymbol) symbolTable.getTableList().get(i)).getConstList()).get(j)).getValue();
                        if (!(value.startsWith("\"") && value.endsWith("\""))) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    default:
                        break;
                }
                j++;
            }
        }
    }

    private void checkClass() {
    }
  
}
