package HackAssembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    private String currentInstruction = "";
    private Scanner scanner;

    /**
     * Opens the input file and prepares to parse it.
     * @param assemblyFile - `.asm` file to be parsed 
     */
    public Parser(File assemblyFile){
        try {
            this.scanner = new Scanner(assemblyFile);
        } catch (FileNotFoundException e) {
            this.scanner = new Scanner("");
        }
    }
    
    /**
     * @return `true` if file input has more lines to be read, else `false`
     */
    public boolean hasMoreLines(){
        return scanner.hasNextLine();
    }

    /**
     * Reads the next line of instruction from the file, sets the line as the
     * current instruction of the `Parser`.
     * Skips over whitespace and comments. 
     */
    public void advance(){
        String instruction;
        do {
            instruction = scanner.nextLine().trim();
        } while ((instruction.isBlank() || instruction.startsWith("//")) && hasMoreLines());
        if (instruction.isBlank() || instruction.startsWith("//")) currentInstruction = "";
        currentInstruction = instruction;
    }

    /**
     * @return `InstructionType`
     *         `A_INSTRUCTION` for `@xxx` instructions, where
     *                         `xxx` is either a decimal number or a symbol
     *         `C_INSTRUCTION` for `dest=comp;jump` instructions
     *         `L_INSTRUCTION` for `(xxx)` instructions where `xxx` is a symbol
     */
    public InstructionType instructionType(){
        if (currentInstruction.startsWith("@", 0)){
            return InstructionType.A_INSTRUCTION;
        } else if (currentInstruction.startsWith("(") && currentInstruction.endsWith(")")){
            return InstructionType.L_INSTRUCTION;
        } else {
            return InstructionType.C_INSTRUCTION;
        }
    }

    /**
     * Called only if instruction is A_INSTRUCTION `@xxx` or L_INSTRUCTION `(xxx)
     * @return `symbol` xxx
     */
    public String symbol(){
        if (instructionType() == InstructionType.A_INSTRUCTION){
            return currentInstruction.substring(1);
        }
        return currentInstruction.substring(1, currentInstruction.lastIndexOf(")")); 
    }

    /**
     * @return symbolic dest part of the current C instruction
     */
    public String dest(){
        int destIndex = currentInstruction.lastIndexOf("=");
        if (destIndex == -1) return "";
        return currentInstruction.substring(0, destIndex);
    }

    /**
     * @return symbolic comp part of the current C instruction
     */
    public String comp(){
        int compStartIndex = currentInstruction.lastIndexOf("=") == -1 ? 0 : currentInstruction.lastIndexOf("=") + 1;
        int compLastIndex = currentInstruction.lastIndexOf(";") == -1 ? currentInstruction.length() : currentInstruction.lastIndexOf(";");
        return currentInstruction.substring(compStartIndex, compLastIndex);
    }

    /**
     * @return symbolic jump of the current C instruction
     */
    public String jump(){
        int jumpStartIndex = currentInstruction.lastIndexOf(";");
        return jumpStartIndex == -1 ? "" : currentInstruction.substring(jumpStartIndex + 1, currentInstruction.length());
    }
}

enum InstructionType {
    A_INSTRUCTION,
    C_INSTRUCTION,
    L_INSTRUCTION
}
