package tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import vmtranslator.VMTranslator;

public class VMTranslatorTest {

    @Test
    public void basicTest(){
        // Arrange
        String tBasicTestAsm = readFile("../../07/BasicTest/BasicTest.test.asm");
        // Act
        new VMTranslator("../../07/BasicTest/BasicTest.vm");
        String basicTestAsm = readFile("../../07/BasicTest/BasicTest.asm");
        // Assert
        Assert.assertEquals(tBasicTestAsm, basicTestAsm);
    }

    private String readFile(String filePath) {
        String content = "";
        try {
            content = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            return "";
        }
        return content;
    }

}
