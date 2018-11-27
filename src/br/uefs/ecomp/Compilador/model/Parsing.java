/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author Natália Rosa e Adriel Brito
 */
public class Parsing {

    private final LinkedList tokenList;
    private LinkedList errorList;
    private int i;

    public Parsing(LinkedList tokenList) {
        this.tokenList = tokenList;
        this.errorList = new LinkedList();
        this.i = 0;
    }

    public boolean controllerParsing() {
        globalStructure();
        return false;
    }

    private void globalStructure() {
        constStructure();
        classStructure();
        moreClassesStructure();
        
        if(!errorList.isEmpty()){
            System.out.println("Erro sitático econtrado!");
            for (Object o : errorList) {
                SyntacticError e = (SyntacticError)o;
                System.out.print("Na linha "+ e.getLine()+ " é esperado: ");
                for (Object o2 : e.getExpectedToken()) {
                    String s = (String)o2;
                    System.out.print("'"+ s +"' ");
                }
                System.out.println(" e consta: '"+ e.getToken().getLexeme()+"'");
            }
        }
    }

    private void constStructure() {
        Stack constStructureStack = new Stack();
        constStructureStack.add(((Token) tokenList.get(i)));
        if ("const".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                constStructureStack.add(((Token) tokenList.get(i)));
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                while (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    constDeclarationStructure();
                }
                if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    constStructureStack.pop(); //Desempilha '}'
                    constStructureStack.pop(); //Desempilha 'const'
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                }
                else{
                    LinkedList l = new LinkedList();
                    l.add("}");
                    errorList.add(new SyntacticError( l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                    while(!"}".equals(((Token) tokenList.get(i)).getLexeme())){i++;}
                }
            }
            else{
                LinkedList l = new LinkedList();
                l.add("{");
                errorList.add(new SyntacticError( l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while(!"}".equals(((Token) tokenList.get(i)).getLexeme())){i++;}
            }
        }
        if (constStructureStack.isEmpty()) {
            System.out.println("SUCESSO em const.");
        }
    }

    private void constDeclarationStructure() {
        if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            moreConstStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    constDeclarationStructure();
                }
            }
            else{
                LinkedList l = new LinkedList();
                l.add(";");
                errorList.add(new SyntacticError( l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
                while(!"}".equals(((Token) tokenList.get(i)).getLexeme())){i++;}
            }
        }
        else{
            LinkedList l = new LinkedList();
            l.add("int");
            l.add("float");
            l.add("boolean");
            errorList.add(new SyntacticError( l, ((Token) tokenList.get(i)).getLine(), ((Token) tokenList.get(i))));
            while(!"}".equals(((Token) tokenList.get(i)).getLexeme())){i++;}
        }
    }

    private void moreConstStructure() {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if ("=".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if ((Type.Identifier.equals(((Token) tokenList.get(i)).getType()))
                        || (Type.Number.equals(((Token) tokenList.get(i)).getType()))
                        || (Type.String.equals(((Token) tokenList.get(i)).getType()))
                        || ("true".equals(((Token) tokenList.get(i)).getLexeme()))
                        || ("false".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        i++;
                        moreConstStructure();
                    }
                }
            }
        }
    }

    private void classStructure() {
        if ("class".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if ((Type.Identifier.equals(((Token) tokenList.get(i)).getType()))) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if ("extends".equals(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    if ((Type.Identifier.equals(((Token) tokenList.get(i)).getType()))) {
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        i++;
                    }
                }
                if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    variableStructure();
                    methodDeclarationStructure();
                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        System.out.println("SUCESSO em classe");
                    }
                }
            }
        }
    }

    private void moreClassesStructure() {
        if (i + 1 < tokenList.size()) {
            i++;
            if ("class".equals(((Token) tokenList.get(i)).getLexeme())) {
                classStructure();
                moreClassesStructure();
            }
        }
    }

    private void variableStructure() {
        if ("variables".equals(((Token) tokenList.get(i)).getLexeme())) {
            if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                while (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    variableDeclarationStructure();
                }
                if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    System.out.println("SUCESSO em variables.");
                }
            }
        }
    }

    private void variableDeclarationStructure() {
        if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            moreVariableStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    variableDeclarationStructure();
                }
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            moreVariableStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                    variableDeclarationStructure();
                }
            }
        }
    }

    private void moreVariableStructure() {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            arrayStructure();
            attributeStructure();
            if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                moreVariableStructure();
            }
        }
    }

    private void methodDeclarationStructure() {
        if ("method".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if (Type.Keyword.equals(((Token) tokenList.get(i)).getType())) {
                if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        i++;
                        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
                            System.out.println(((Token) tokenList.get(i)).getLexeme());
                            i++;
                            while (!(")".equals(((Token) tokenList.get(i)).getLexeme()))) {
                                methodParameterDeclarationStructure();
                            }
                            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                                System.out.println(((Token) tokenList.get(i)).getLexeme());
                                i++;
                                if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                                    i++;
                                    variableStructure();
                                    while (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                                        commandsStructure();
                                    }
                                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                                        System.out.println("sucesso em method.");
                                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void methodParameterDeclarationStructure() {
        if (keywordType(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    methodParameterDeclarationStructure();
                }
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    methodParameterDeclarationStructure();
                }
            }
        }
    }

    private void commandsStructure() {
        switch (((Token) tokenList.get(i)).getLexeme()) {
            case "if":
                ifelseStructure();
                commandsStructure();
                break;
            case "while":
                whileStructure();
                commandsStructure();
                break;
            case "write":
                writeStructure();
                commandsStructure();
                break;
            case "read":
                readStructure();
                commandsStructure();
                break;
            case "return":
                returnStructure();
                commandsStructure();
        }
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())
                ||   "--".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())) {
            atributtionStructure();
            if (";".equals(((Token) tokenList.get(i)).getLexeme())){
                i++;
                commandsStructure();
            }

        }
    }

    private void atributtionStructure() {
        if ("--".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())) {
            i++;
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                arrayStructure();
                attributeStructure();
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            arrayStructure();
            attributeStructure();
            verificationSctructure();
        }
    }

    private void verificationSctructure() {
        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            complementStructure();
        } else if ("--".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
        } else if ("=".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            normalAtributtionStructure();
        }
    }

    private void normalAtributtionStructure() {
        if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
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
        }
    }

    private void returnStructure() {
        if ("return".equals(((Token) tokenList.get(i)).getLexeme())) {
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
                    i++;
                }
            }
        }
    }

    private void ifelseStructure() { //Recebe posição do token na lista de tokens
        Stack ifStructureStack = new Stack();
        ifStructureStack.add(((Token) tokenList.get(i)));
        System.out.println(((Token) tokenList.get(i)).getLexeme());
        i++;

        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            ifStructureStack.add(((Token) tokenList.get(i)));
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            expressionStructure();

            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                ifStructureStack.pop();
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;

                if ("then".equals(((Token) tokenList.get(i)).getLexeme())) {
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                        ifStructureStack.add(((Token) tokenList.get(i)));
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        i++;
                        commandsStructure();
                        if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            ifStructureStack.pop(); //Desempilha '}'
                            ifStructureStack.pop(); //Desempilha 'if'
                            System.out.println(((Token) tokenList.get(i)).getLexeme());
                            i++;
                        }
                    }
                }
            }
        }

        if (ifStructureStack.isEmpty()) {
            System.out.println("SUCESSO em if.");
            if (tokenList.size() > i) {
                if ("else".equals(((Token) tokenList.get(i)).getLexeme())) {
                    Stack elseStructureStack = new Stack();
                    elseStructureStack.add(((Token) tokenList.get(i)));
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                        elseStructureStack.add(((Token) tokenList.get(i)));
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        i++;
                        while (!("}".equals(((Token) tokenList.get(i)).getLexeme()))) {
                        }

                        if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                            elseStructureStack.pop(); //Desempilha '}'
                            elseStructureStack.pop(); //Desempilha 'else'
                            System.out.println(((Token) tokenList.get(i)).getLexeme());
                            i++;
                        }
                    }
                    if (elseStructureStack.isEmpty()) {
                        System.out.println("SUCESSO em else");
                    }
                }
            }
        }
    }

    private void whileStructure() {
        Stack whileStructureStack = new Stack();
        whileStructureStack.add(((Token) tokenList.get(i)));
        System.out.println(((Token) tokenList.get(i)).getLexeme());
        i++;

        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            whileStructureStack.add(((Token) tokenList.get(i)));
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            expressionStructure();
            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                whileStructureStack.pop();
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                if ("{".equals(((Token) tokenList.get(i)).getLexeme())) {
                    whileStructureStack.add(((Token) tokenList.get(i)));
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                    commandsStructure();
                    if ("}".equals(((Token) tokenList.get(i)).getLexeme())) {
                        whileStructureStack.pop(); //Desempilha '}'
                        whileStructureStack.pop(); //Desempilha 'while'
                        System.out.println(((Token) tokenList.get(i)).getLexeme());
                        i++;
                    }
                }
            }
        }
        if (whileStructureStack.isEmpty()) {
            System.out.println("SUCESSO em while.");
        }
    }

    private void writeStructure() {
        Stack writeStructureStack = new Stack();
        writeStructureStack.add(((Token) tokenList.get(i)));
        System.out.println(((Token) tokenList.get(i)).getLexeme());
        i++;
        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            writeStructureStack.add(((Token) tokenList.get(i)));
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;

            while (!(")".equals(((Token) tokenList.get(i)).getLexeme()))) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                writeparameterStructure();
            }

            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                writeStructureStack.pop();
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;

                if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                    writeStructureStack.pop();
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                }
            }
        }
        if (writeStructureStack.isEmpty()) {
            System.out.println("SUCESSO em write.");
        }
    }

    private void readStructure() {
        Stack readStructureStack = new Stack();
        readStructureStack.add(((Token) tokenList.get(i)));
        System.out.println(((Token) tokenList.get(i)).getLexeme());
        i++;
        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            readStructureStack.add(((Token) tokenList.get(i)));
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;

            while (!(")".equals(((Token) tokenList.get(i)).getLexeme()))) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                readparameterStructure();
            }

            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                readStructureStack.pop();
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;

                if (";".equals(((Token) tokenList.get(i)).getLexeme())) {
                    readStructureStack.pop();
                    System.out.println(((Token) tokenList.get(i)).getLexeme());
                    i++;
                }
            }
        }
        if (readStructureStack.isEmpty()) {
            System.out.println("SUCESSO em read.");
        }
    }

    private void writeparameterStructure() {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            arrayStructure();
            attributeStructure();
            write2Structure();
        } else if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            write2Structure();
        }
    }

    private void readparameterStructure() {
        if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            arrayStructure();
            attributeStructure();
            read2Structure();
        }
    }

    private void write2Structure() {
        if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            writeparameterStructure();
        }
    }

    private void read2Structure() {
        if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            readparameterStructure();
        }
    }

    private void attributeStructure() {
        if (".".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
                arrayStructure();
                attributeStructure();
            } else {
                System.out.println("Falha na chamada do atributo.");
            }
        }
    }

    private void arrayStructure() {
        if ("[".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            while (!("]".equals(((Token) tokenList.get(i)).getLexeme()))) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
            }
            //chamar metodo de expressoes aritimeticas
            if ("]".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
            }
        }
        if ("[".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            while (!("]".equals(((Token) tokenList.get(i)).getLexeme()))) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
            }
            //chamar metodo de expressoes aritimeticas
            if ("]".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
            }
        }
    }

    private void expressionStructure() {
        addStructure();
        relacionalStructure();
    }

    private void relacionalStructure() {
        if (Type.RelationalOperator.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            addStructure();
            logicalStructure();
        } else {
            logicalStructure();
        }

    }

    private void logicalStructure() {
        if ("||".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            expressionStructure();
        } else if ("&&".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            expressionStructure();
        }
    }

    private void addStructure() {
        multStrucute();
        dStructure();
    }

    private void dStructure() {
        if ("+".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            addStructure();
        } else if ("-".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            addStructure();
        }
    }

    private void multStrucute() {
        negStrucute();
        eStructure();
    }

    private void eStructure() {
        if ("*".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            multStrucute();
        } else if ("/".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            multStrucute();
        }
    }

    private void negStrucute() {
        if ("-".equals(((Token) tokenList.get(i)).getLexeme())
                || "!".equals(((Token) tokenList.get(i)).getLexeme())
                || "++".equals(((Token) tokenList.get(i)).getLexeme())
                || "--".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
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

    private void gStructure() {
        if ("--".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
        } else if ("--".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
        }
    }

    private void expValueStructure() {
        if (Type.Number.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
        } else if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            expressionStructure();
            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
            }
        } else if (Type.Identifier.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            arrayStructure();
            attributeStructure();
            complementStructure();
        } else if ("true".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
        } else if ("false".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
        }
    }

    private void complementStructure() {
        if ("(".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            ParamStrucuture();
            if (")".equals(((Token) tokenList.get(i)).getLexeme())) {
                System.out.println(((Token) tokenList.get(i)).getLexeme());
                i++;
            }
        }
    }

    private void ParamStrucuture() {
        if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
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
        }
    }

    private void moreParamSctruture() {
        if (",".equals(((Token) tokenList.get(i)).getLexeme())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            obrigatoryParamStrucuture();
        }
    }

    private void obrigatoryParamStrucuture() {
        if (Type.String.equals(((Token) tokenList.get(i)).getType())) {
            System.out.println(((Token) tokenList.get(i)).getLexeme());
            i++;
            moreParamSctruture();
        } else {
            expressionStructure();
            moreParamSctruture();
        }
    }

    private boolean keywordType(String lexeme) {
        return (lexeme.equals("int")) || (lexeme.equals("float")) || (lexeme.equals("bool")) || (lexeme.equals("string")) || (lexeme.equals("void"));
    } //Verifica se é uma keyword de tipagem

}
