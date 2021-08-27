package com.SpriteSheetToPNG;

import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConvertState {
    public static void main(String[] args) throws IOException {


        int versionNumber = 1;
        System.out.println("Sprite sheet to PNG sequence, version " + versionNumber);

        int frameLimit = 99; //limitFramesInput();

        int[] frameLimitArray;
        frameLimitArray = new int[3];
            //    frameLimitArray = limitFramesInput(); // THIS ONE
        frameLimitArray[0] = 40; // if it contains IDLE
        frameLimitArray[1] = 20; // because comamnd prompt isn't working, I will go with this
        frameLimitArray[2] = 99; // for all customs

        // System.out.println("return = "+frameLimit);

        // PASSING IN A COMMAND LINE WILL PASS THROUGH AS AN ARGS PARAMETER.
        // This makes your "typed in" split into an array of args, one for each word separated by a space.
        // Once I learn about something I never have to learn it again.
        // JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        // int result = fileChooser.showOpenDialog( parent );
        System.out.println("A file opener should have opened. Select an XML or PNG file of a sprite you want to convert. The XML and PNG files MUST be in the same folder!!!!");
        FileDialog dialog = new FileDialog((Frame) null, "Select XML or PNG File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);


   /*
*/

        // we forget about the file opener here

        String filedir = dialog.getDirectory(); // the file directory, excluding thefile itself
        String filename = dialog.getFile(); // gets the file name

        String fnlc = filename.toLowerCase();
        // If it's a PNG, change the reference to the XML. If it isn't an XML otherwise, stop the program.
        if(fnlc.endsWith("png")){
            filename = removeLastFour(filename) + ".xml";
        } else if(!fnlc.endsWith("xml")){
            System.out.println("You didn't select a PNG or an XML. Exiting program.");
            System.exit(69420);
        }

        System.out.println(filename + " chosen.");


        String trueXML = filedir + filename; // trueXML is our file path
        String pngFileDir = removeLastFour(trueXML) + ".png";
        String spriteSheetName = removeLastFour(filename);

        System.out.println("the corresponding PNG file directory " + pngFileDir);
        System.out.println(trueXML + " is the location of the file itself.");
        System.out.println("filedir is: " + filedir + " and filename is: " + filename);

        String folderin = null;

        // create a new folder and that's where everything is going to be put. The work directory always has a trailing backslash
        String workDirectory = newFolder(filedir, spriteSheetName) + "\\";
        System.out.println(workDirectory);

        // Loading the image

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pngFileDir));
        } catch (IOException e) {
            System.out.println("You selected an XML file without its corresponding PNG file in the same folder. Exiting program.");
            System.exit(666);
        }
// img is globally the "image" we are referencing
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(trueXML)); // THE XML FILE IS THE "DOC" a.k.a. DOCUMENT!!!

            doc.getDocumentElement().normalize();



            // get <staff>
            NodeList list = doc.getElementsByTagName("SubTexture");

            for (int temp = 0; temp < list.getLength(); temp++) {
                // System.out.println("in the for loop");
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // starts off at the 0th thing we're checking
                    System.out.println(temp);
                    // get staff's attribute
                    String framename = element.getAttribute("name"); // this works perfectly
                    String lastFourDigits = "";
                    if (framename.length() > 4) {
                        lastFourDigits = framename.substring(framename.length() - 4);
                    } else {
                        lastFourDigits = framename;
                    }

                    int animNo = Integer.parseInt(lastFourDigits);

/*
                    // Useless stuff
                    String frw = (element.getAttribute("frameWidth"));
                    String frh = (element.getAttribute("frameHeight"));
                    String frx = (element.getAttribute("frameX"));
                    String fry = (element.getAttribute("frameY"));
                    String numberx = (element.getAttribute("x"));
                    String numbery = (element.getAttribute("y"));
                    String justwidth = (element.getAttribute("width"));
                    String justheight = (element.getAttribute("height"));

                    System.out.println("frameWidth: " + frw + " frameHeight:" + frh);
                    System.out.println("frameX: " + frx + " frameY:" + fry);
                    System.out.println("x: " + frameWidth + " y:" + frameHeight);
                    System.out.println("width: " + frameWidth + " height:" + frameHeight);
                    // DEBUG END
*/
int frameWidth;
int frameHeight;
int frameX;
int frameY;
int x;
int y;
int width;
int height;

                    width = Integer.parseInt(element.getAttribute("width")); // size of the bounding box
                    height = Integer.parseInt(element.getAttribute("height"));

                    // sometimes, the XML may emit framewidths or frameheights. you must catch them to avoid them turning out an error.

                    try { frameWidth = Integer.parseInt(element.getAttribute("frameWidth")); // the width and height of the image being displayed
                    } catch(NumberFormatException e){
                        frameWidth = width;
                    }


                    try{ frameHeight = Integer.parseInt(element.getAttribute("frameHeight"));
                    } catch(NumberFormatException e){frameHeight = height;
                    } //

                  try{   frameX = Integer.parseInt(element.getAttribute("frameX"));
                  }catch(NumberFormatException e){frameX = 0;
                  } // adjusts character positioning
                    try{  frameY = Integer.parseInt(element.getAttribute("frameY"));
                    }catch(NumberFormatException e){frameY = 0;
                    }

                     x = Integer.parseInt(element.getAttribute("x")); // top left corner of the bounding box
                     y = Integer.parseInt(element.getAttribute("y"));


/*
                    System.out.println("frameWidth: " + frameWidth + " frameHeight:" + frameHeight);
                    System.out.println("frameX: " + frameX + " frameY:" + frameY);
                    System.out.println("x: " + x + " y:" + y);
                    System.out.println("width: " + width + " height:" + height);
                    // only do this when it detects a new
*/
                    if (animNo == 0 || folderin == null) {
                        // filedir is our directory
                        String foldername = removeLastFour(framename); // something like BfLEFT
// workDirectory has a backslash appended to it
                    //    String newdirname = workDirectory + foldername; // directory of our new folder

                        String currentAnimationFolder = newFolder(workDirectory, foldername);

                        folderin = currentAnimationFolder; // folderin is the directory we want to be currently exporting our PNG files
                    }
                    // making the image

                    // frameLimitArray[0] is standard, frameLimitArray[1] is nonIdle, or stricter.
                    // frameLimitArray[0] is used if the animation name contains the word "Idle", otherwise it is frameLimitArray[1]
                    // if that is BELOW the frame limit
                    String lcframename = framename.toLowerCase();
                    Boolean idling = lcframename.contains("idle"); // true when idle
                    Boolean lrdu = lcframename.contains("left") || lcframename.contains("right") || lcframename.contains("up") || lcframename.contains("down");


                    int frlimit;
                    if(idling){
                        frlimit = frameLimitArray[0];
                    } else if(lrdu) {
                        frlimit = frameLimitArray[1];
                    } else {
                        frlimit = frameLimitArray[2];
                    }

                if (animNo <= frlimit) {
                    int dstx1 = -frameX; // horizontal offset
                    int dsty1 = -frameY; // vertical offset
                    int dstx2 = -frameX + width; // horizontal offset second axis
                    int dsty2 = -frameY + height; // vertical offset second axis

                    int srcx1 = x;
                    int srcx2 = x + width;
                    int srcy1 = y;
                    int srcy2 = y + height;

                    BufferedImage image = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB); // heck it 8 bits image
                    Graphics2D g2d = image.createGraphics();
                    // the PNG file is currently a directory. we need an image
                    System.out.println("source pixels are (" + srcx1 + ", " + srcy1 + ") and (" + srcx2 + ", " + srcy2 + ")");
                    g2d.drawImage(img, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2, null);


// folderin does not have a backslash appended to the back of it

                    try (FileOutputStream fos = new FileOutputStream(folderin + "\\" + framename + ".png")) {
                        ImageIO.write(image, "PNG", fos);
                    } catch (IOException e) {

                        // handle exception
                    }

                    g2d.dispose();
                    // folderin is where to export the images to


                    // create new folder
                    // direct all paths here
                    // create image like normal

                    // put everything in the last folder
                    // create image like normal
                    System.out.println(framename);
                } else System.out.println("Skipping "+ framename);


                }
            }

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            System.out.println("Either you selected a PNG file with no corresponding XML file, or the XML file you selected wasn't an XML file.\nBoth the PNG and XML file of a sprite must be in the same folder, and they both must have the same name.\nExiting program.");
            System.exit(1337);
            e.printStackTrace();
        }

       System.out.println("End of program");
        System.exit(10);
    }

    private static String newFolder(String existingPath, String newFolderName) throws IOException {
        // returns the path of the new folder
        // the existing path must end with a backslash \
        String newdirname = existingPath + newFolderName; // directory of our new folder
        System.out.println("Creating new folder " + newdirname);
        Path thepath = Paths.get(newdirname);
        if (!Files.exists(thepath)) {
            Files.createDirectory(thepath);
        } else {
            System.out.println("Directory already exists. Stopping program...");
           System.exit(200);
        }
        return newdirname;

    }

    private static String removeLastFour(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 4);
        }
        return result;
    }

    private static int[] limitFramesInput(){
        System.out.println("Sprites are usually animated in 24FPS; however, they're mostly done on twos. If your animation is a two-framer, I would type four instead." +
                "\nBecause idle animations are usually 5 frames, I would at least make this value 10, or to be safe, 20." +
                "\nThis value is ZERO-BASED. What you type will be seen as what you typed + 1.");
        System.out.println("You will input two values. They will be seperated by space characters" +
                "\nThe first value is the frame limit for idle animations, " +
                "and the second value is the frame limit for arrow and non-idle animations." +
                "\n Defaults are 40, 12, 40. Type a non-number in the cmd line to force defaults." +
                "\nIdle animations are typically 10 frames, while arrow animations are typically 4 frames:");
        Scanner scanner = new Scanner(System.in); // this will take care of the details from getting the input from the user
        String userInput = scanner.nextLine();
        String[] parts = userInput.split(" "); // creates a new array that hosts each "word" that is split by a space
        System.out.println("You typed: " + parts[0]);
        int typedinput; // max frame limiter overall
        try {
            typedinput = Integer.parseInt(parts[0]); // throw NumberFormatException;
            if (typedinput <= 0) {
                System.out.println("Below zero! Defaulting to 99...");
                typedinput = 99;
            }
        } catch (NumberFormatException e){
                System.out.println("Not an integer! Setting defaults");
                typedinput = 99;
                int[] defaultarray;
                defaultarray = new int[3];
                defaultarray[0] = 40;
                defaultarray[1] = 12;
                defaultarray[2] = 40;
                return defaultarray;


        }
        int nonidleinput;
        try {
            nonidleinput = Integer.parseInt(parts[1]); // throw NumberFormatException;
            if (nonidleinput <= 0) {
                System.out.println("Below zero! Defaulting to typedinput...");
                nonidleinput = typedinput;
            }
        } catch (NumberFormatException e){
            System.out.println("Not an integer! Defaulting to typedinput");
            nonidleinput = typedinput;
        }
        int custominput;
        try {
            custominput = Integer.parseInt(parts[2]); // throw NumberFormatException;
            if (custominput <= 0) {
                System.out.println("Below zero! Defaulting to typedinput...");
                custominput = typedinput;
            }
        } catch (NumberFormatException e){
            System.out.println("Not an integer! Defaulting to typedinput");
            custominput = typedinput;
        }

int limray[];
        limray = new int[3];
        limray[0] = typedinput;
        limray[1] = nonidleinput;
        limray[2] = custominput;




        System.out.println("Returning...");
        return limray;
    }

    /*


    private static void perAnimation(int placeHoldeded){
        // called per ANIMATION.
        int frameWidth = // TBA
        int frameHeight = // TBA

*/


   /* private static void perFrame(int placeHolded) {

        // called per FRAME within an ANIMATION.
        // all variables on the right are included in the XML per line
        int dstx1 = -frameX;
        int dsty1 = -frameY;
        int dstx2 = -frameX + width;
        int dsty2 = -frameY + height;
        int srcx1 = x;
        int srcx2 = y;
        int srcy1 = x + width;
        int srcy2 = x + height;

        BufferedImage image = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB); // heck it 8 bits image
        Graphics2D g2d = image.createGraphics();

        g2d.drawImage(spriteSHEET, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2, null);

    }  */
}



