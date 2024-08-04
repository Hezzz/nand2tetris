package HackAssembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HackAssembler {

    private final File assemblyFile; 
    private final SymbolTable symbolTable;

    /**
     * Opens the input file (../xxx.asm) and gets ready to process it.
     * Constructs a symbol table and adds all predefined symbols
     * @param filePath - filepath of the assembly file
     */
    public HackAssembler(String filePath){
        this.assemblyFile = new File(filePath);
        this.symbolTable = new SymbolTable();
    }

    /**
     * Reads the program lines, one by one
     * focused only on (label) declarations.
     * Adds the found labels to the symbol table
     */
    public void firstPass(){
        Parser parser = new Parser(assemblyFile);
        while(parser.hasMoreLines()){
            parser.advance();
            InstructionType currentInstructionType = parser.instructionType();
            if (currentInstructionType == InstructionType.L_INSTRUCTION){
                String symbol = parser.symbol();
                int address = 0;
                symbolTable.addEntry(symbol, address);
            }
        }
    }

    /**
     * Main loop.ary value.instructions.
     * Translates each into it binary value.
     * Writes into an output file.
     * Writes into an output file.
     */
    public void secondPass(){
        Parser parser = new Parser(assemblyFile);
        StringBuilder content = new StringBuilder();
        while(parser.hasMoreLines()){
            parser.advance();
            InstructionType currentInstructionType = parser.instructionType();
            if (currentInstructionType == InstructionType.A_INSTRUCTION || currentInstructionType == InstructionType.L_INSTRUCTION){
                String symbol = parser.symbol();
                if (symbolTable.contains(symbol)){
                    int address = symbolTable.getAddress(symbol);
                    String binaryValue = String.format("%16s", Integer.toBinaryString(address)).replace(' ', '0');
                    content.append(binaryValue).append(System.lineSeparator());
                } else {
                    symbolTable.addEntry(symbol, 0);
                }
            } else if (currentInstructionType == InstructionType.C_INSTRUCTION){
                String dest = parser.dest();
                String comp = parser.comp();
                String jump = parser.jump();
                String destBinaryValue = Code.dest(dest);
                String compBinaryValue = Code.comp(comp);
                String jumpBinaryValue = Code.jump(jump);
                String binaryValue = "111" + compBinaryValue + destBinaryValue + jumpBinaryValue;
                content.append(binaryValue).append(System.lineSeparator());
            }
        }
        writeHackFile(content.toString());
    }

    private void writeHackFile(String content){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../../06/Test.hack"))){
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
