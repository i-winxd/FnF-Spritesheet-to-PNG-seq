# Friday Night Funkin' Spritesheet to PNG sequence (and also GIFs, indirectly)
Breaks apart a Friday Night Funkin' spritesheet back to its individual components in the form of a PNG sequence. This is intended if you want to work with Friday Night Funkin' animations outside the game, such as uploading animations in GIF format to the Friday Night Funkin' wiki.

## How do I use this thing?

To run this program, go to releases (https://github.com/i-winxd/FnF-Spritesheet-to-PNG-seq/releases/tag/v1), and download the topmost ZIP file. Extract the .jar file and run it.

You need to have Java installed on your computer to run this program.

When running this program, it will prompt you to load your PNG/XML spritesheet. For the spritesheet you want to target, the PNG and XML file of it **must** be in the same folder.
As obvious, select your PNG/XML (it doesn't matter which one), and it will generate a folder in the same directory of the PNG/XML with folders containing image sequences of each animation.

For example, if my PNG/XML is in \[...\]/Some FnF mod/assets/shared/images/characters/bob.png, selecting it creates a folder in the same directory as bob.png with the the name "bob". Inside the new "bob" folder, you should see folders of all their animations (e.g. one of them may look like "bob LEFT") 

### Reasons why the program might not work and troubleshooting
- You didn't select a PNG or XML file
- You selected a PNG file with no corresponding XML file in the **same** folder, or you selected an XML file with no corresponding PNG
- There is a folder in the same directory with the same name as your character. **This code just basically stops when it tries to create a folder that already exists** rather than referencing one, and yes, I coded that on purpose.

### Important!
Currently, this program will only export
- The first 20 frames of any arrow animation (contains, to lower case "left", "down", "up", "right")
- The first 40 frames of any idle animation (contains, to lower case "idle")
- The first 99 frames of all other animations

### Also important!
The only way to close this program (unless it closes itself, e.g. when it is done converting it will exit) is by opening up task manager and closing it through that method.

## Very cool, but how do I make GIFs with this thing?
[Here.](https://ezgif.com/maker) This convers PNG sequences to GIFs. Remember that you must select ALL FRAMES when importing the PNG sequence. You must also:
- Set the delay time to "3"
- Turn ON "don't stack frames"

## To do list:
- Add a branch which allows usage of the command line prompt to make changing export frame limits customizable
- Append something to the end of the created folder to indicate that it was generated using this tool
- GIF export support

## What do the XML variables mean?
- `frameWidth/frameHeight` - how big the animation is total, per animation. the size is calculated by the smallest rectangle you can make over all frames of the same animation stacked on top of each other.
- `frameX/frameY` - mini offset per animation
- `width/height` - width/height of the single frame.
- `x/y` - the top left corner of the animation in the base PNG file, per frame.

