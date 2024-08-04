package tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import HackAssembler.HackAssembler;

public class HackAssemblerTests {

    private String addHackTest;
    private String maxHackTest;
    private String maxLHackTest;
    private String pongHackTest;
    private String pongLHackTest;
    private String rectHackTest;
    private String rectLHackTest;

    @Before
    public void setUp(){
        addHackTest = readFile("../../06/Add.test.hack");
        maxHackTest = readFile("../../06/Max.test.hack");
        maxLHackTest = readFile("../../06/MaxL.test.hack");
        pongHackTest = readFile("../../06/Pong.test.hack");
        pongLHackTest = readFile("../../06/PongL.test.hack");
        rectHackTest = readFile("../../06/Rect.test.hack");
        rectLHackTest = readFile("../../06/RectL.test.hack");
    }

    @Test
    public void addTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/Add.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String addHack = readFile("../../06/Add.Hack");
        Assert.assertEquals(addHackTest, addHack);
    }

    @Test
    public void maxTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/Max.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String maxHack = readFile("../../06/Max.hack");
        Assert.assertEquals(maxHackTest, maxHack);
    }

    @Test
    public void maxLTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/MaxL.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String maxLHack = readFile("../../06/MaxL.hack");
        Assert.assertEquals(maxLHackTest, maxLHack);
    }

    @Test
    public void pongTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/Pong.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String pongHack = readFile("../../06/Pong.hack");
        Assert.assertEquals(pongHackTest, pongHack);
    }

    @Test
    public void pongLTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/PongL.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String pongLHack = readFile("../../06/PongL.hack");
        Assert.assertEquals(pongLHackTest, pongLHack); 
    }

    @Test
    public void rectTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/Rect.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String rectHack = readFile("../../06/Rect.hack");
        Assert.assertEquals(rectHackTest, rectHack);
    } 

    @Test
    public void rectLTest(){
        HackAssembler hackAssembler = new HackAssembler("../../06/RectL.asm");
        hackAssembler.firstPass();
        hackAssembler.secondPass();
        String rectLHack = readFile("../../06/RectL.hack");
        Assert.assertEquals(rectLHackTest, rectLHack);
    }

    private String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
           String line;
           while ((line = br.readLine()) != null) {
            content.append(line).append(System.lineSeparator());
           } 
        } catch (IOException e) {
            return "";
        }
        return content.toString();
    }
}
