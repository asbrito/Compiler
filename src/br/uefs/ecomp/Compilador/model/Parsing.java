/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author Natália Rosa e Adriel Brito
 */
public class Parsing {

    private LinkedList tokenList;
    private LinkedList errorList;
    private int i;
    private String nameFile;

    public Parsing(LinkedList tokenList, String name) {
        this.tokenList = tokenList;
        this.errorList = new LinkedList();
        this.i = 0;
        this.nameFile = name;
    }

    public boolean controllerParsing(LinkedList tokenList, String name) throws IOException {
        tokenList = tokenList;
        errorList = new LinkedList();
        i = 0;
        globalStructure();
        return false;
    }

    private void globalStructure() throws IOException {
        try {
            constStructure();
            classStructure();
            moreClassesStructure();
        } catch (IndexOutOfBoundsException ex) {
            return;
        }
        printError();
    }

    private void constStructure() throws IOException, IndexOutOfBoundsException {
        if ("const".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                constDeclarationStructure();
                if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                } else {
                    LinkedList l = new LinkedList();
                    l.add("}");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                }
            } else {
                LinkedList l = new LinkedList();
                l.add("{");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void constDeclarationStructure() throws IOException, IndexOutOfBoundsException {
        if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            moreConstStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                if (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    constDeclarationStructure();
                }
            }
        } 
    }

    private void moreConstStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            if ("=".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                if ((Type.Identifier.equals(((Token) tokenList.get(i)).getType()))
                        || (Type.Number.equals(((Token) tokenList.get(i)).getType()))
                        || (Type.String.equals(((Token) tokenList.get(i)).getType()))
                        || ("true".equals(((Token) tokenList.get(i)).getLexeme()))
                        || ("false".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    increment();
                    if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                        moreConstStructure();
                    }
                }
            }
        }
    }

    private void classStructure() throws IOException, IndexOutOfBoundsException {
        if ("class".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if ((Type.Identifier.equals(((Token) tokenList.get(i)).getType()))) {
                increment();
                if ("extends".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    if ((Type.Identifier.equals(((Token) tokenList.get(i)).getType()))) {
                        increment();
                    } else {
                        LinkedList l = new LinkedList();
                        l.add("Tipo Identificador");
                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                        while (!"class".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                        }
                        if("class".equals(((Token) tokenList.get(i)).getLexeme())){
                            classStructure();
                        }
                    }
                }
                if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    variableStructure();
                    methodDeclarationStructure();
                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        moreClassesStructure();
                    }
                    else {
                        LinkedList l = new LinkedList();
                        l.add("}");
                        if (incrementCheck(i)){
                            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                            while(!"class".equals(((Token) tokenList.get(i)).getLexeme())) {
                                increment();
                            }
                            if("class".equals(((Token) tokenList.get(i)).getLexeme())){
                                classStructure();
                            }
                        } 
                        else{
                            errorList.add(new SyntacticError(l, i));
                            printError();
                            throw new IndexOutOfBoundsException();
                        }
                    }
                } else {
                    LinkedList l = new LinkedList();
                    l.add("{");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!"class".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                    if("class".equals(((Token) tokenList.get(i)).getLexeme())){
                        classStructure();
                    }
                }
            } else {
                LinkedList l = new LinkedList();
                l.add("Tipo Identificador");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"class".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
                if("class".equals(((Token) tokenList.get(i)).getLexeme())){
                    classStructure();
                }
            }
        } else {
            LinkedList l = new LinkedList();
            l.add("class");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!"class".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
            if("class".equals(((Token) tokenList.get(i)).getLexeme())){
                classStructure();
            }
        }
    }

    private void moreClassesStructure() throws IOException, IndexOutOfBoundsException {
        int j = i +1;
        if (incrementCheck(j)) {
            increment();
            if ("class".equals(((Token) tokenList.get(i)).getLexeme())) {
                classStructure();
                moreClassesStructure();
            }
        } else{
            printError();
            throw new IndexOutOfBoundsException();
        }
    }

    private void variableStructure() throws IOException, IndexOutOfBoundsException {
        if ("variables".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                variableDeclarationStructure();
            } else {
                LinkedList l = new LinkedList();
                l.add("{");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void variableDeclarationStructure() throws IOException, IndexOutOfBoundsException {
        if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            moreVariableStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                if (keywordType(((Token) tokenList.get(i)).getLexeme())
                        || Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                    variableDeclarationStructure();
                } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                } else {
                    LinkedList l = new LinkedList();
                    l.add("}");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                }
            } else {
                LinkedList l = new LinkedList();
                l.add(";");
                l.add(",");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }

        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            moreVariableStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                if (keywordType(((Token) tokenList.get(i)).getLexeme())
                        || Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                    variableDeclarationStructure();
                } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                } else {
                    LinkedList l = new LinkedList();
                    l.add("}");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                }
            } else {
                LinkedList l = new LinkedList();
                l.add(";");
                l.add(",");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        } else {
            LinkedList l = new LinkedList();
            l.add("int");
            l.add("float");
            l.add("boolean");
            l.add("String");
            l.add("Tipo Identificador");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void moreVariableStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            arrayStructure();
            attributeStructure();
            if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                moreVariableStructure();
            }
        } else {
            LinkedList l = new LinkedList();
            l.add("Tipo Indentificador");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"}".equals(((Token) tokenList.get(i)).getLexeme())
                    && !";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void methodDeclarationStructure() throws IOException, IndexOutOfBoundsException {
        if ("method".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if (Type.Keyword.equals(((Token) tokenList.get(i)).getType())) {
                if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                        increment();
                        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                            while (!(")".equals(((Token) tokenList.get(i)).getLexeme()))) {
                                methodParameterDeclarationStructure();
                            }
                            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                                increment();
                                if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                                    increment();
                                    variableStructure();
                                    while (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                                        commandsStructure();
                                    }
                                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                                        int j = i +1;
                                        if (incrementCheck(j)) {
                                            increment();
                                            if ("method".equals(((Token) tokenList.get(i)).getLexeme())) {
                                                methodDeclarationStructure();
                                            }
                                        }
                                        else{
                                            LinkedList l = new LinkedList();
                                            l.add("}");
                                            errorList.add(new SyntacticError(l, j));
                                            printError();
                                            throw new IndexOutOfBoundsException();
                                        }
                                    }else{
                                        LinkedList l = new LinkedList();
                                        l.add("}");
                                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                                        methodError();
                                    }
                                }else{
                                    LinkedList l = new LinkedList();
                                    l.add("{");
                                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                                    methodError();
                                }
                            }else{
                                LinkedList l = new LinkedList();
                                l.add(")");
                                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                                methodError();
                            }
                        }else{
                            LinkedList l = new LinkedList();
                            l.add("(");
                            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                            methodError();
                        }
                    }
                    else{
                        LinkedList l = new LinkedList();
                        l.add("Identifier");
                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                        methodError();
                    }
                }
                else{
                    LinkedList l = new LinkedList();
                    l.add("void || bool || int || string || float");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    methodError();
                }
            }
            else {
                LinkedList l = new LinkedList();
                l.add("void || bool || int || string || float");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                methodError();
            }
        }
    }

    private void methodError() throws IOException, IndexOutOfBoundsException{
        while (!"method".equals(((Token) tokenList.get(i)).getLexeme())
            && !"class".equals(((Token) tokenList.get(i)).getLexeme())
            && incrementCheck(i)) {
            increment();
        }
        if ("method".equals(((Token) tokenList.get(i)).getLexeme())) {
            methodDeclarationStructure();
        } else if ("class".equals(((Token) tokenList.get(i)).getLexeme())){
            classStructure();
        }
    }
    
    private void methodParameterDeclarationStructure() throws IOException, IndexOutOfBoundsException {
        if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                increment();
                arrayStructure();
                if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    methodParameterDeclarationStructure();
                }
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                increment();
                arrayStructure();
                if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    methodParameterDeclarationStructure();
                }
            }
        }
    }

    private void commandsStructure() throws IOException, IndexOutOfBoundsException {
        if ("if".equals(((Token) tokenList.get(i)).getLexeme())) {
            ifStructure();
            commandsStructure();
        } else if ("while".equals(((Token) tokenList.get(i)).getLexeme())) {
            whileStructure();
            commandsStructure();
        } else if ("write".equals(((Token) tokenList.get(i)).getLexeme())) {
            writeStructure();
            commandsStructure();
        } else if ("read".equals(((Token) tokenList.get(i)).getLexeme())) {
            readStructure();
            commandsStructure();
        } else if ("return".equals(((Token) tokenList.get(i)).getLexeme())) {
            returnStructure();
            increment();
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                || "--".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())) {
            atributtionStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                commandsStructure();
            }
            else{
                LinkedList l = new LinkedList();
                l.add(";");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            }
        }
        else{
            if(!("}".equals(((Token) tokenList.get(i)).getLexeme()))){
                LinkedList l = new LinkedList();
                l.add("if || while || write || read || return || Identifier");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                increment();
            }
        }
    }

    private void atributtionStructure() throws IOException, IndexOutOfBoundsException {
        if ("--".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                increment();
                arrayStructure();
                attributeStructure();
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            arrayStructure();
            attributeStructure();
            verificationSctructure();
        } else {
            LinkedList l = new LinkedList();
            l.add("--");
            l.add("++");
            l.add("Tipo Indentificador");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void verificationSctructure() throws IOException, IndexOutOfBoundsException {
        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            complementStructure();
        } else if ("--".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
        } else if ("=".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            normalAtributtionStructure();
        } else {
            LinkedList l = new LinkedList();
            l.add("--");
            l.add("++");
            l.add("=");
            l.add("(");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                
                increment();
            }
        }
    }

    private void normalAtributtionStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            increment();
        } else if ("-".equals(((Token) tokenList.get(i)).getLexeme())
                || "--".equals(((Token) tokenList.get(i)).getLexeme())
                || "!".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())
                || "(".equals(((Token) tokenList.get(i)).getLexeme())
                || "false".equals(((Token) tokenList.get(i)).getLexeme())
                || "true".equals(((Token) tokenList.get(i)).getLexeme())
                || Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                || Type.Number.equals(((Token) tokenList.get(i)).getType())) {
            expressionStructure();
        } else {
            LinkedList l = new LinkedList();
            l.add("-");
            l.add("--");
            l.add("++");
            l.add("!");
            l.add("(");
            l.add("false");
            l.add("true");
            l.add("Tipo Intificador");
            l.add("Tipo String");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void returnStructure() throws IOException, IndexOutOfBoundsException {
        if ("return".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if ("-".equals(((Token) tokenList.get(i)).getLexeme())
                    || "--".equals(((Token) tokenList.get(i)).getLexeme())
                    || "!".equals(((Token) tokenList.get(i)).getLexeme())
                    || "++".equals(((Token) tokenList.get(i)).getLexeme())
                    || "(".equals(((Token) tokenList.get(i)).getLexeme())
                    || "false".equals(((Token) tokenList.get(i)).getLexeme())
                    || "true".equals(((Token) tokenList.get(i)).getLexeme())
                    || Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                    || Type.Number.equals(((Token) tokenList.get(i)).getType())) {
                expressionStructure();
                if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void ifStructure() throws IOException, IndexOutOfBoundsException {
        increment();

        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            expressionStructure();

            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();

                if ("then".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                        commandsStructure();
                        if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                            if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                                elseStructure();
                            }
                        } else {
                            LinkedList l = new LinkedList();
                            l.add("}");
                            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                            while (!"else".equals(((Token) tokenList.get(i)).getLexeme())
                                    && !"if".equals(((Token) tokenList.get(i)).getLexeme())
                                    && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                                    && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                                    && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                                    && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                                increment();
                            }
                            if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                                elseStructure();
                            } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                                increment();
                                if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                                    elseStructure();
                                }
                            } else {
                                commandsStructure();
                            }
                        }
                    } else {
                        LinkedList l = new LinkedList();
                        l.add("{");
                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                        while (!"else".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"if".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                        }
                        if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                            elseStructure();
                        } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                            if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                                elseStructure();
                            }
                        }
                        commandsStructure();
                    }
                } else {
                    LinkedList l = new LinkedList();
                    l.add("then");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!"else".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"if".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                    if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                        elseStructure();
                    } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                        if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                            elseStructure();
                        }
                    }
                    commandsStructure();
                }
            } else {
                LinkedList l = new LinkedList();
                l.add(")");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"else".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"if".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
                if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                    elseStructure();
                } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                        elseStructure();
                    }
                }
                commandsStructure();
            }
        } else {
            LinkedList l = new LinkedList();
            l.add("(");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!"else".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"if".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
            if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                elseStructure();
            } else if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                    elseStructure();
                }
            }
            commandsStructure();
        }
    }
    
    private void elseStructure() throws IOException, IndexOutOfBoundsException {
        increment();
        if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            commandsStructure();
            if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            } else {
                LinkedList l = new LinkedList();
                l.add("}");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"if".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
                if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
                commandsStructure();
            }
        } else {
            LinkedList l = new LinkedList();
            l.add("{");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!"if".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
            if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
            commandsStructure();
        }
    }

    private void whileStructure() throws IOException, IndexOutOfBoundsException {
        Stack whileStructureStack = new Stack();
        whileStructureStack.add(((Token) tokenList.get(i)));
        increment();

        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            whileStructureStack.add(((Token) tokenList.get(i)));
            increment();
            expressionStructure();
            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                whileStructureStack.pop();
                increment();
                if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                    whileStructureStack.add(((Token) tokenList.get(i)));
                    increment();
                    commandsStructure();
                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        whileStructureStack.pop(); //Desempilha '}'
                        whileStructureStack.pop(); //Desempilha 'while'
                        increment();
                    } else {
                        LinkedList l = new LinkedList();
                        l.add("}");
                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                        while (!"if".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                        }
                        if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                        }
                        commandsStructure();
                    }
                } else {
                    LinkedList l = new LinkedList();
                    l.add("{");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!"if".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                    commandsStructure();
                }
            }
        }
        if (whileStructureStack.isEmpty()) {
        }
    }

    private void writeStructure() throws IOException, IndexOutOfBoundsException {
        if ("write".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                writeparameterStructure();
                if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                    writeparameterStructure();
                    if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    } else {
                        LinkedList l = new LinkedList();
                        l.add(";");
                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                        while (!";".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                        }
                    }
                } else {
                    LinkedList l = new LinkedList();
                    l.add(")");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!";".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                }

            } else {
                LinkedList l = new LinkedList();
                l.add("(");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!";".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void readStructure() throws IOException, IndexOutOfBoundsException {
        if ("read".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
                readparameterStructure();
                if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();

                    if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    } else {
                        LinkedList l = new LinkedList();
                        l.add(";");
                        errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                        while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                                && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                            increment();
                        }
                    }
                } else {
                    LinkedList l = new LinkedList();
                    l.add(")");
                    errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                            && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                        increment();
                    }
                }
            } else {
                LinkedList l = new LinkedList();
                l.add("(");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void writeparameterStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            arrayStructure();
            attributeStructure();
            write2Structure();
        } else if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            write2Structure();
        } else {
            LinkedList l = new LinkedList();
            l.add("Tipo Indentificador");
            l.add("String");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void readparameterStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            arrayStructure();
            attributeStructure();
            read2Structure();
        } else {
            LinkedList l = new LinkedList();
            l.add("Tipo Indentificador");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                    && !")".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void write2Structure() throws IOException, IndexOutOfBoundsException {
        if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            writeparameterStructure();
        } else {
            if (!",".equals(((Token) tokenList.get(i)).getLexeme())) {
                LinkedList l = new LinkedList();
                l.add(",");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void read2Structure() throws IOException, IndexOutOfBoundsException {
        if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            readparameterStructure();
        } else {
            if (!",".equals(((Token) tokenList.get(i)).getLexeme())) {
                LinkedList l = new LinkedList();
                l.add(",");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void attributeStructure() throws IOException, IndexOutOfBoundsException {
        if (".".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                increment();
                arrayStructure();
                attributeStructure();
            } else {
                LinkedList l = new LinkedList();
                l.add("Tipo Indentificador");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"(".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && ",".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void arrayStructure() throws IOException, IndexOutOfBoundsException {
        if ("[".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            addStructure();
            if ("]".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            } else {
                LinkedList l = new LinkedList();
                l.add("]");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"(".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && ",".equals(((Token) tokenList.get(i)).getLexeme())
                        && ".".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
        if ("[".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            addStructure();
            if ("]".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            } else {
                LinkedList l = new LinkedList();
                l.add("]");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!"(".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && ",".equals(((Token) tokenList.get(i)).getLexeme())
                        && ".".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void expressionStructure() throws IOException, IndexOutOfBoundsException {
        addStructure();
        relacionalStructure();
    }

    private void relacionalStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.RelationalOperator.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            addStructure();
            logicalStructure();
        } else {
            logicalStructure();
        }

    }

    private void logicalStructure() throws IOException, IndexOutOfBoundsException {
        if ("||".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            expressionStructure();
        } else if ("&&".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            expressionStructure();
        }
    }

    private void addStructure() throws IOException, IndexOutOfBoundsException {
        multStrucute();
        dStructure();
    }

    private void dStructure() throws IOException, IndexOutOfBoundsException {
        if ("+".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            addStructure();
        } else if ("-".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            addStructure();
        }
    }

    private void multStrucute() throws IOException, IndexOutOfBoundsException {
        negStrucute();
        eStructure();
    }

    private void eStructure() throws IOException, IndexOutOfBoundsException {
        if ("*".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            multStrucute();
        } else if ("/".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            multStrucute();
        }
    }

    private void negStrucute() throws IOException, IndexOutOfBoundsException {
        if ("-".equals(((Token) tokenList.get(i)).getLexeme())
                || "!".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())
                || "--".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            expValueStructure();
        } else if ("(".equals(((Token) tokenList.get(i)).getLexeme())
                || "false".equals(((Token) tokenList.get(i)).getLexeme())
                || "true".equals(((Token) tokenList.get(i)).getLexeme())
                || Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                || Type.Number.equals(((Token) tokenList.get(i)).getType())) {
            expValueStructure();
            gStructure();
        }
    }

    private void gStructure() throws IOException, IndexOutOfBoundsException {
        if ("--".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
        } else if ("--".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
        }
    }

    private void expValueStructure() throws IOException, IndexOutOfBoundsException {
        if (Type.Number.equals(((Token) tokenList.get(i)).getType())) {
            increment();
        } else if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            expressionStructure();
            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            arrayStructure();
            attributeStructure();
            complementStructure();
        } else if ("true".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
        } else if ("false".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
        } else {
            LinkedList l = new LinkedList();
            l.add("true");
            l.add("false");
            l.add("Tipo Identificador");
            l.add("Number");
            l.add("(");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!"else".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"if".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"while".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"write".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"read".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"method".equals(((Token) tokenList.get(i)).getLexeme())
                    && ";".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private void complementStructure() throws IOException, IndexOutOfBoundsException {
        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            ParamStrucuture();
            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            } else {
                LinkedList l = new LinkedList();
                l.add(")");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!")".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())
                        && "{".equals(((Token) tokenList.get(i)).getLexeme())
                        && ",".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void ParamStrucuture() throws IOException, IndexOutOfBoundsException {
        if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            moreParamSctruture();
        } else if ("-".equals(((Token) tokenList.get(i)).getLexeme())
                || "--".equals(((Token) tokenList.get(i)).getLexeme())
                || "!".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())
                || "(".equals(((Token) tokenList.get(i)).getLexeme())
                || "false".equals(((Token) tokenList.get(i)).getLexeme())
                || "true".equals(((Token) tokenList.get(i)).getLexeme())
                || Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                || Type.Number.equals(((Token) tokenList.get(i)).getType())) {
            expressionStructure();
            moreParamSctruture();
        } else {
            LinkedList l = new LinkedList();
            l.add("-");
            l.add("--");
            l.add("++");
            l.add("!");
            l.add("(");
            l.add("false");
            l.add("true");
            l.add("Tipo Intificador");
            l.add("Tipo String");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }

    }

    private void moreParamSctruture() throws IOException, IndexOutOfBoundsException {
        if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
            increment();
            obrigatoryParamStrucuture();
        }
        else {
            if (!",".equals(((Token) tokenList.get(i)).getLexeme())) {
                LinkedList l = new LinkedList();
                l.add(",");
                errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while (!";".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                        && !"method".equals(((Token) tokenList.get(i)).getLexeme())) {
                    increment();
                }
            }
        }
    }

    private void obrigatoryParamStrucuture() throws IOException, IndexOutOfBoundsException {
        if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            increment();
            moreParamSctruture();
        } else if ("-".equals(((Token) tokenList.get(i)).getLexeme())
                || "--".equals(((Token) tokenList.get(i)).getLexeme())
                || "!".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())
                || "(".equals(((Token) tokenList.get(i)).getLexeme())
                || "false".equals(((Token) tokenList.get(i)).getLexeme())
                || "true".equals(((Token) tokenList.get(i)).getLexeme())
                || Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                || Type.Number.equals(((Token) tokenList.get(i)).getType())) {
            expressionStructure();
            moreParamSctruture();
        } else {
            LinkedList l = new LinkedList();
            l.add("true");
            l.add("false");
            l.add("Tipo Identificador");
            l.add("Number");
            l.add("(");
            l.add("Tipo String");
            errorList.add(new SyntacticError(l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while (!")".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"class".equals(((Token) tokenList.get(i)).getLexeme())
                    && !"method".equals(((Token) tokenList.get(i)).getLexeme())
                    && "{".equals(((Token) tokenList.get(i)).getLexeme())
                    && ",".equals(((Token) tokenList.get(i)).getLexeme())) {
                increment();
            }
        }
    }

    private boolean keywordType(String lexeme) {
        return (lexeme.equals("int")) || (lexeme.equals("float")) || (lexeme.equals("bool")) || (lexeme.equals("string")) || (lexeme.equals("void"));
    }

    private void increment() throws IOException, IndexOutOfBoundsException{
        i++;
        if (!incrementCheck(i)) {
            errorList.add(new SyntacticError(i-1));
            printError();
            throw new IndexOutOfBoundsException();
       }
    }
    
    private void printError() throws IOException {
        File file = new File ("teste");
        file.mkdir();
        String dir = file.getAbsolutePath();
        FileWriter file2 = new FileWriter(dir+"\\Output_Syntactic_"+nameFile);
        PrintWriter writefile = new PrintWriter(file2);
        if (!errorList.isEmpty()) {
            writefile.println("Erro sitático econtrado!");
            for (Object o : errorList) {
                SyntacticError e = (SyntacticError) o;
                writefile.println("Na linha " + e.getLine() + " é esperado: ");
                for (Object o2 : e.getExpectedToken()) {
                    String s = (String) o2;
                    writefile.print("'" + s + "' ");
                }
                writefile.println(" e consta: '" + e.getToken().getLexeme() + "'");
            }
        } else {
            writefile.println("SUCESSO! Nenhum erro sitático econtrado!");
        }

        file2.close();
    }
     
    private boolean incrementCheck(int i){
        return tokenList.size() > i;
    }

}
