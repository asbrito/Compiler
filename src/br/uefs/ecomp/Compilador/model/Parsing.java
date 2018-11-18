/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author nati_
 */
public class Parsing {
    LinkedList tokenList;
    public Parsing(LinkedList tokenList) {
        this.tokenList = tokenList;
    }
    
    public void controllerParsing(int i){
        Type type;
        switch(((Token)tokenList.get(i)).getType()){

        }
    }
    
    public int ifStructure (int i){ //Recebe posição do token na lista de tokens
        Stack ifStructureStack = new Stack ();
        ifStructureStack.add(((Token)tokenList.get(i)));
        i++;

        if ("(".equals(((Token)tokenList.get(i)).getLexeme())){ 
            ifStructureStack.add(((Token)tokenList.get(i)));
            i++;

            while (!(")".equals(((Token)tokenList.get(i)).getLexeme()))){i++;}

            if (")".equals(((Token)tokenList.get(i)).getLexeme())){ 
                ifStructureStack.pop();
                i++;

                if ("then".equals(((Token)tokenList.get(i)).getLexeme())){
                    i++;
                    if ("{".equals(((Token)tokenList.get(i)).getLexeme())){ 
                        ifStructureStack.add(((Token)tokenList.get(i)));
                        i++;
                        while (!("}".equals(((Token)tokenList.get(i)).getLexeme()))){
                            switch(((Token)tokenList.get(i)).getLexeme()){
                                case "if":
                                    int structureValidation = ifStructure (i);
                                    if (structureValidation == 0){System.out.println("Erro em if!");}
                                break;
                            }
                            i++;
                        }
                        if ("}".equals(((Token)tokenList.get(i)).getLexeme())){ 
                            ifStructureStack.pop(); //Desempilha '}'
                            ifStructureStack.pop(); //Desempilha 'if'
                            i++;
                        }
                    }
                }
            }
        }
        if (ifStructureStack.isEmpty()){
            
            if ("else".equals(((Token)tokenList.get(i)).getLexeme())){ 
                int structureCall = elseStructure(i);
                i = structureCall;
            }
            
            System.out.println("SUCESSO em if.");
            return i; // retorna lugar que parou
        }
        return 0;
    }
    public int elseStructure (int i){
        Stack elseStructureStack = new Stack ();
        if ("else".equals(((Token)tokenList.get(i)).getLexeme())){ 
            elseStructureStack.add(((Token)tokenList.get(i)));
            i++;
            if ("{".equals(((Token)tokenList.get(i)).getLexeme())){ 
                elseStructureStack.add(((Token)tokenList.get(i)));
                i++;
                while (!("}".equals(((Token)tokenList.get(i)).getLexeme()))){
                    switch(((Token)tokenList.get(i)).getLexeme()){
                        case "if":
                            int structureValidation = ifStructure (i);
                            i = structureValidation;
                            //if (structureValidation == 0){System.out.println("Erro em else!");}
                        break;
                    }
                    i++;
                }
                
                if ("}".equals(((Token)tokenList.get(i)).getLexeme())){ 
                    elseStructureStack.pop(); //Desempilha '}'
                    elseStructureStack.pop(); //Desempilha 'else'
                    i++;
                }
                
            }
            if (elseStructureStack.isEmpty()){
                System.out.println("SUCESSO em else");
                return i;
            }
        }
        return 0;
    }
    
    public boolean expressionStructure (int i){
        
        return false;
    }
      
}
