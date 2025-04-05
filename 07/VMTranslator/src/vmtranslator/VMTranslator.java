package vmtranslator;

import java.io.File;

public class VMTranslator {

    public static void main(String[] args) {
        String filePath = args[0];
        new VMTranslator(filePath);
    }

    public VMTranslator(String filePath) {
        File vmFile = new File(filePath);
        Parser parser = new Parser(vmFile);
        // create output file
        File outputFile = getOutputFile(vmFile);
        CodeWriter writer = new CodeWriter(outputFile);

        translate(parser, writer);
    }

    private File getOutputFile(File vmFile) {
        String fileName = vmFile.getName();
        String asmFileName = fileName.substring(0, fileName.lastIndexOf("."));
        return new File("../../07/" + asmFileName + "/" + asmFileName + ".asm");
    }

    private void translate(Parser parser, CodeWriter writer) {
        while (parser.hasMoreLines()) {
            parser.advance();
            CommandType currentCommandType = parser.commandType();
            if (currentCommandType == CommandType.C_ARITHMETIC) {
                String command = parser.arg1();
                writer.writeArithmetic(command);
            } else if (currentCommandType == CommandType.C_PUSH || currentCommandType == CommandType.C_POP) {
                String segment = parser.arg1();
                int index = parser.arg2();
                writer.writePushPop(currentCommandType, segment, index);
            } else {
                break;
            }
        }
        writer.close();
    }
}

enum CommandType {
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABEL,
    C_GOTO,
    C_IF,
    C_FUNCTION,
    C_RETURN,
    C_CALL
}
