
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HuffmanCompressor {
    public static void main (String [] args)
    {
        try
        {
            compress("happy hip hop.txt");
            expand("happy hip hop.code","happy hip hop.txt");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File could not be found!");
        }
    }
    public static void compress (String filename) throws FileNotFoundException {

        Scanner input = new Scanner (new File(filename));
        int [] frequencies = new int [256];
        String nameOnly = filename.substring(0,filename.indexOf(".txt"));
        while (input.hasNextLine())
        {
            String str = input.nextLine();
            char [] chars = str.toCharArray();
            for (int i = 0; i<chars.length; i++)
            {
                frequencies [(int)chars[i]] ++;
            }
            if (input.hasNextLine())
            {
                frequencies[(int)'\n'] ++;
            }
        }
        HuffmanTree tree = new HuffmanTree(frequencies);
        BitOutputStream out = new BitOutputStream(filename);
        tree.write(nameOnly + ".code");
        tree.encode(out, nameOnly + ".short");

    }
    public static void expand (String codeFile, String fileName) throws FileNotFoundException {

        HuffmanTree tree = new HuffmanTree (codeFile);
        String nameOnly = fileName.substring(0,fileName.indexOf(".code"));
        BitInputStream in = new BitInputStream(nameOnly + ".short");
        tree.decode(in, nameOnly + ".new");

    }
}
