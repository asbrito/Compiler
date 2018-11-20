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
    private LinkedList tokenList;
    private int i;
    public Parsing(LinkedList tokenList) {
        this.tokenList = tokenList;
        this.i = 0;
    }
    
    public boolean controllerParsing(){
        Type type;
        while(tokenList.size() > i){
            switch(((Token)tokenList.get(i)).getType()){
                case Keyword:
                    switch (((Token)tokenList.get(i)).getLexeme()){
                        case "if":
                            return ifelseStructure ();
                        case "while":
                            return whileStructure ();
                        case "write":
                            break;
                        case "read":
                            break;
                    }
            }
            return true;
        }
        return false;
    }
    
    public boolean ifelseStructure (){ //Recebe posição do token na lista de tokens
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
                            controllerParsing();
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
            System.out.println("SUCESSO em if.");
            if (!(tokenList.isEmpty())){
                if("else".equals(((Token)tokenList.get(i)).getLexeme())){
                    Stack elseStructureStack = new Stack ();
                    elseStructureStack.add(((Token)tokenList.get(i)));
                    i++;
                    if ("{".equals(((Token)tokenList.get(i)).getLexeme())){ 
                        elseStructureStack.add(((Token)tokenList.get(i)));
                        i++;
                        while (!("}".equals(((Token)tokenList.get(i)).getLexeme()))){
                            controllerParsing();
                        }

                        if ("}".equals(((Token)tokenList.get(i)).getLexeme())){ 
                            elseStructureStack.pop(); //Desempilha '}'
                            elseStructureStack.pop(); //Desempilha 'else'
                            i++;
                        }
                    }
                    if (elseStructureStack.isEmpty()){
                        System.out.println("SUCESSO em else");
                    }
                }
            }
            return controllerParsing(); 
        }
        return false;
    }
    
    public boolean whileStructure (){
        Stack whileStructureStack = new Stack ();
        whileStructureStack.add(((Token)tokenList.get(i)));
        i++;

        if ("(".equals(((Token)tokenList.get(i)).getLexeme())){ 
            whileStructureStack.add(((Token)tokenList.get(i)));
            i++;
            while (!(")".equals(((Token)tokenList.get(i)).getLexeme()))){i++;}
            if (")".equals(((Token)tokenList.get(i)).getLexeme())){ 
                whileStructureStack.pop();
                i++;
                if ("{".equals(((Token)tokenList.get(i)).getLexeme())){ 
                    whileStructureStack.add(((Token)tokenList.get(i)));
                    i++;
                    while (!("}".equals(((Token)tokenList.get(i)).getLexeme()))){
                        controllerParsing();
                    }
                    if ("}".equals(((Token)tokenList.get(i)).getLexeme())){ 
                        whileStructureStack.pop(); //Desempilha '}'
                        whileStructureStack.pop(); //Desempilha 'while'
                        i++;
                    }
                }
            }
        }
        if (whileStructureStack.isEmpty()){
            System.out.println("SUCESSO em while.");
            return controllerParsing(); 
        }
        return false;
    }
}
