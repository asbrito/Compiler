/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import br.uefs.ecomp.Compilador.model.Symbols.ClassSymbol;
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
        typeCheckConst();

    }

    private void typeCheckConst() {
        int j = 0;
        while (symbolTable.getConstList().size() > j){
            if ((symbolTable.getConstList().get(j)) instanceof  ConstantSymbol) {
                switch (((ConstantSymbol)symbolTable.getConstList().get(j)).getType()) {
                    case "int": {
                        String value = ((ConstantSymbol)symbolTable.getConstList().get(j)).getValue();
                        try {
                            int valueInt = Integer.parseInt(value);
                        } catch (java.lang.NumberFormatException ex) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    case "float": {
                        String value = ((ConstantSymbol)symbolTable.getConstList().get(j)).getValue();
                        if (!(value.contains("."))) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    case "bool": {
                        String value = ((ConstantSymbol)symbolTable.getConstList().get(j)).getValue();
                        if (!(value.equals("true") || value.equals("false"))) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    case "void":
                        System.out.println("Void não pode.");
                        break;
                    case "string": {
                        String value = ((ConstantSymbol)symbolTable.getConstList().get(j)).getValue();
                        if (!(value.startsWith("\"") && value.endsWith("\""))) {
                            System.out.println("Erro: tipagem não corresponde ao valor declarado.");
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            j++;
        }
    }

    private void checkClass() {
        int j = 0;
        if (symbolTable.getClassList().get(j) instanceof ClassSymbol){
            if (((ClassSymbol)symbolTable.getClassList().get(j)).getMotherClass() != null){
                String motherClassName = ((ClassSymbol)symbolTable.getClassList().get(j)).getMotherClass();
                boolean exist = false;
                
                for (int k = j; k < 0; k--){
                    if (((ClassSymbol)symbolTable.getClassList().get(k)).getIdentifier().equals(motherClassName)){
                        exist = true;
                        break;
                    }
                }
                
                if(!exist){
                    System.out.println("Erro: Classe mãe não declarada.");
                }
            } else {
                int n = 0;
                while (((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().size() > n){
                    
                }
            }
        }
    }
  
}
