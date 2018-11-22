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
                            ifelseStructure ();
                            break;
                        case "while":
                            whileStructure ();
                            break;
                        case "write":
                            writeStructure();
                            break;
                        case "read":
                            readStructure();
                            break;
                        case "const":
                            constStructure();
                            break;
                    }
            }
            return true;
        }
        return false;
    }
    
    private void ifelseStructure (){ //Recebe posição do token na lista de tokens
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
            if (tokenList.size()>i){
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
                        controllerParsing();
                    }
                }
            }
            controllerParsing();
        }
    }
    
    private void whileStructure (){
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
            controllerParsing();
        }
    }
    
    private void constStructure(){
        Stack constStructureStack = new Stack ();
        constStructureStack.add(((Token)tokenList.get(i)));
        i++;
        if ("{".equals(((Token)tokenList.get(i)).getLexeme())){ 
            constStructureStack.add(((Token)tokenList.get(i)));
            i++;
            while (!("}".equals(((Token)tokenList.get(i)).getLexeme()))){
                constDeclarationStructure();
            }
            if ("}".equals(((Token)tokenList.get(i)).getLexeme())){ 
                constStructureStack.pop(); //Desempilha '}'
                constStructureStack.pop(); //Desempilha 'const'
                i++;
            }
        }
        if (constStructureStack.isEmpty()){
            System.out.println("SUCESSO em const.");
            controllerParsing();
        }
    }
    
    private void constDeclarationStructure(){
        if(Type.Keyword.equals(((Token)tokenList.get(i)).getType())){
            if(keywordType(((Token)tokenList.get(i)).getLexeme())){
                i++;
                moreConstStructure();
                if (";".equals(((Token)tokenList.get(i)).getLexeme())){
                    i++;
                    if (!("}".equals(((Token)tokenList.get(i)).getLexeme()))){ 
                        constDeclarationStructure();
                    }
                }
            }
        }
    }
    
    private void moreConstStructure(){
        if(Type.Identifier.equals(((Token)tokenList.get(i)).getType())){
            i++;
            if ("=".equals(((Token)tokenList.get(i)).getLexeme())){
                i++;
                if((Type.Identifier.equals(((Token)tokenList.get(i)).getType())) ||
                (Type.Number.equals(((Token)tokenList.get(i)).getType())) ||
                ("true".equals(((Token)tokenList.get(i)).getLexeme())) ||
                ("false".equals(((Token)tokenList.get(i)).getLexeme()))){
                    i++;
                    if (",".equals(((Token)tokenList.get(i)).getLexeme())){
                        moreConstStructure();
                    }
                }
            }
        }
    }
    
    private boolean keywordType(String lexeme){
        return (lexeme.equals("int")) || (lexeme.equals("float")) || (lexeme.equals("bool")) || (lexeme.equals("string")) || (lexeme.equals("void"));
    } //Verifica se é uma keyword de tipagem
    
    
    private void writeStructure() {
        Stack writeStructureStack = new Stack ();
        writeStructureStack.add(((Token)tokenList.get(i)));
        i++;
        if ("(".equals(((Token)tokenList.get(i)).getLexeme())){ 
            writeStructureStack.add(((Token)tokenList.get(i)));
            i++;

            while (!(")".equals(((Token)tokenList.get(i)).getLexeme()))){
                i++;
                writeparameterStructure();
            }

            if (")".equals(((Token)tokenList.get(i)).getLexeme())){ 
                writeStructureStack.pop();
                i++;

                if (";".equals(((Token)tokenList.get(i)).getLexeme())){ 
                    writeStructureStack.pop();
                    i++;
                }
            }
        }
        if (writeStructureStack.isEmpty()){
           System.out.println("SUCESSO em write.");
           controllerParsing();
        }
    }
    
    
    private void readStructure() {
        Stack readStructureStack = new Stack ();
        readStructureStack.add(((Token)tokenList.get(i)));
        i++;
        if ("(".equals(((Token)tokenList.get(i)).getLexeme())){ 
            readStructureStack.add(((Token)tokenList.get(i)));
            i++;

            while (!(")".equals(((Token)tokenList.get(i)).getLexeme()))){
                i++;
                readparameterStructure();
            }

            if (")".equals(((Token)tokenList.get(i)).getLexeme())){ 
                readStructureStack.pop();
                i++;

                if (";".equals(((Token)tokenList.get(i)).getLexeme())){ 
                    readStructureStack.pop();
                    i++;
                }
            }
        }
        if (readStructureStack.isEmpty()){
           System.out.println("SUCESSO em read.");
           controllerParsing();
        }
    }
    
    
    private void writeparameterStructure() {
        if(Type.Identifier.equals(((Token)tokenList.get(i)).getType())){
            i++;
            arrayStructure();
            attributeStructure();
            write2Structure();
        }
        else if(Type.String.equals(((Token)tokenList.get(i)).getType())){
            write2Structure();
        }
    }
    
    private void readparameterStructure() {
        if(Type.Identifier.equals(((Token)tokenList.get(i)).getType())){
            i++;
            arrayStructure();
            attributeStructure();
            read2Structure();
        }
    }
    
    private void write2Structure() {
        if(",".equals(((Token)tokenList.get(i)).getLexeme())){
            i++;
            writeparameterStructure();
        }
    }
    
    private void read2Structure() {
        if(",".equals(((Token)tokenList.get(i)).getLexeme())){
            i++;
            readparameterStructure();
        }
    }
    
    private void attributeStructure() {
        if(".".equals(((Token)tokenList.get(i)).getLexeme())){
            i++;
            arrayStructure();
            attributeStructure();
        }
    }

    private void arrayStructure() {
        if("[".equals(((Token)tokenList.get(i)).getLexeme())){
            i++;
            while (!("]".equals(((Token)tokenList.get(i)).getLexeme()))){i++;}
            //chamar metodo de expressoes aritimeticas
            if("]".equals(((Token)tokenList.get(i)).getLexeme())){
                i++;
            }
        }
        if("[".equals(((Token)tokenList.get(i)).getLexeme())){
            i++;
            while (!("]".equals(((Token)tokenList.get(i)).getLexeme()))){i++;}
            //chamar metodo de expressoes aritimeticas
            if("]".equals(((Token)tokenList.get(i)).getLexeme())){
                i++;
            }
        }   
    }
}
