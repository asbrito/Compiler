/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import br.uefs.ecomp.Compilador.model.Symbols.ClassSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.ConstantSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.MethodSymbol;
import br.uefs.ecomp.Compilador.model.Symbols.Symbol;
import br.uefs.ecomp.Compilador.model.Symbols.SymbolTable;
import br.uefs.ecomp.Compilador.model.Symbols.VariablesSymbol;

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
        int j = 0; //Lugar atual da leitura da classe
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
                int n = 0; //Lugar atual da leitura de método
                while (((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().size() > n){
                    //Variables
                    int m = 0; //Lugar atual da leitura de variáveis
                    while((((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getVariablesList().size() > m)){
                        String typeVariable = ((VariablesSymbol)((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getVariablesList().get(m)).getType();
                        if ((!"int".equals(typeVariable)) && (!"float".equals(typeVariable)) && (!"bool".equals(typeVariable)) && (!"string".equals(typeVariable))){
                            boolean exist = false;
                            for (int k = j; k < 0; k--){
                                if (((ClassSymbol)symbolTable.getClassList().get(k)).getIdentifier().equals(typeVariable)){
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                System.out.println("Erro: Tipagem desconhecida.");
                            }
                        } 
                        if (((VariablesSymbol)((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getVariablesList().get(m)).getArray() != 0) {
                            if (((VariablesSymbol)((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getVariablesList().get(m)).getSizeArray() < 0){
                                System.out.println("Erro: Índice negativo.");
                            }
                            if (((VariablesSymbol)((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getVariablesList().get(m)).getSizeTwoArray() < 0){
                                System.out.println("Erro: Índice negativo.");
                            }
                        }
                        m++;
                    }
                    int l = 0; // leitura de parâmetros
                    while((((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getParameterList().size() > l)){
                        String typeParameter = ((Symbol)((MethodSymbol)((ClassSymbol)symbolTable.getClassList().get(j)).getMethodList().get(n)).getParameterList().get(l)).getType();
                        if ((!"int".equals(typeParameter)) && (!"float".equals(typeParameter)) && (!"bool".equals(typeParameter)) && (!"string".equals(typeParameter))){
                            boolean exist = false;
                            for (int k = j; k < 0; k--){
                                if (((ClassSymbol)symbolTable.getClassList().get(k)).getIdentifier().equals(typeParameter)){
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                System.out.println("Erro: Tipagem desconhecida.");
                            }
                        }
                        l++;
                    }
                }
            }
        }
        j++;
    }
  
}
