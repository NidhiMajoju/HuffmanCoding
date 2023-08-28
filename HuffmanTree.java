import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HuffmanTree
{
    private Queue<Node> nodes= new PriorityQueue<>();
    private Node tree;
    private String [] codes;

    public HuffmanTree (int [] counts)
    {
        nodes = new PriorityQueue<>();
        codes = new String [257];
        for (int i = 0; i<counts.length; i++)
        {
            if (counts[i] == 0)
            {
                continue;
            }
            nodes.add(new Node(counts[i],i));
        }
        //EOF value
        nodes.add(new Node (-1, 256));
        while (nodes.size()>1)
        {
            Node node1 = nodes.poll();
            Node node2 = nodes.poll();
            Node temp = new Node (node1.getFreq()+node2.getFreq());
            temp.left = node1;
            temp.right = node2;
            nodes.offer(temp);
        }
    }
    public HuffmanTree (String codeFile) throws FileNotFoundException {
        // BitInputStream in = new BitInputStream(codeFile);
        Scanner input = new Scanner (new File (codeFile));
        codes = new String [257];
        // construct the top node
        tree = new Node (-1);
        //do I need to factor in line breaks here
        //I think that the line break is already encoded and is part of this (mental note)
        while (input.hasNext())
        {
            int n = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            codes [n] = code;
            if (tree == null)
            {
                tree = new Node (-1);
            }
            else {
                tree = HuffmanTreeHelper(tree, code, n, 0 );

            }
        }
        //in.close();
    }
    private Node HuffmanTreeHelper (Node node, String bits, int val, int index)
    {
        if (index == bits.length() - 1)
        {
            if (bits.charAt(index) == '0')
            {
                node.left = new Node (-1, val);
            }
            else
            {
                node.right = new Node (-1, val);
            }
            return node;
        }
        else if (bits.charAt(index) == '0')
        {
            node.left = HuffmanTreeHelper(node.left, bits, val, index +1);
        }
        else
        {
            node.right = HuffmanTreeHelper(node.right, bits, val, index +1);
        }
        return node;

    }

    public void write (String fileName)
    {
        String outputFileName = fileName;
        PrintWriter diskFile = null;
        try
        {
            diskFile = new PrintWriter(new File(outputFileName));
            Node root = nodes.peek();
            writeHelper(diskFile, root,"");
            diskFile.close();
        }
        catch (IOException io)
        {
            System.out.println("Could not create file: " + outputFileName);
        }


    }
    private void writeHelper (PrintWriter diskFile, Node node, String binary)
    {
        if (node == null)
        {
            return;
        }

        if (node.left == null && node.right == null)
        {
            diskFile.println(node.element);
            diskFile.println(binary);
            codes [node.element] = binary;
        }

        String copy = binary;
        writeHelper(diskFile, node.left, copy + "0");
        binary = copy;
        writeHelper(diskFile, node.right, binary + "1");
        return;
    }
    //use BitOutPutStream
    public void encode (BitOutputStream out, String fileName) throws FileNotFoundException {
        // BitOutputStream stream = new BitOutputStream(fileName);
        Scanner input = new Scanner (new File (fileName));
        Node root = nodes.peek();
        while (input.hasNextLine())
        {
            String code = input.nextLine() + "\n";
            char[] chars = code.toCharArray();
            for (int i = 0; i<chars.length; i++)
            {
                String s = codes [i];
                while (s.length() != 0 )
                {
                    int x = Integer.parseInt(s.substring(0,1));
                    out.writeBit(x);
                    s = s.substring(1);
                }
                //int value = (int)chars[i];
                //encodeHelper(root, value, out);
            }
            String eof = codes [256];
            while (eof.length() != 0)
            {
                int x = Integer.parseInt(eof.substring(0,1));
                out.writeBit(x);
                eof = eof.substring(1);
            }
        }
        //add the EOF
        //encodeHelper(root, 256, out);
        out.close();
    }
    //before tracking the codes in an array
// private void encodeHelper (Node node, int val, BitOutputStream stream)
// {
// if (node == null)
// {
// return ;
// }
// if (node.element == val)
// {
// return;
// }
//
// encodeHelper(node.left, val, stream);
// stream.writeBit(0);
// encodeHelper(node.right, val, stream);
// stream.writeBit(1);
// return;
// }
    public void decode (BitInputStream in, String outFile) throws FileNotFoundException {

            PrintWriter diskFile = new PrintWriter(new File(outFile));
            Node temp = tree;

            while (true)
            {
                while (temp.left != null && temp.right != null)
                {
                    int bit = in.readBit();
                    if (bit == -1 || temp.element == 256)
                    {
                        break;
                    }
                    if (bit == 0)
                    {
                        temp = temp.left;
                    }
                    else if (bit == 1)
                    {
                        temp = temp.right;
                    }
                }
                if (temp.element == 256)
                {
                    break;
                }
                diskFile.println((char)temp.element);
                temp = tree;
            }
            diskFile.close();
            //in.close();


    }

}