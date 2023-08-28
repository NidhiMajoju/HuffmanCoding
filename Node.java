public class Node<T> implements Comparable<Node>{

    @Override
    public int compareTo(Node o) {
        if (this.freq < o.getFreq())
        {
            return -1;
        }
        if (this.freq > o.getFreq())
        {
            return 1;
        }
        return 0;
    }
    public int freq;
    int element;
    public Node right;
    public Node left;
    public Node (int val, Integer element)
    {
        this.element = element ;
        this.freq = val;
        left = right = null;
    }
    public Node (int val)
    {
        this.freq = val;
        element = -1;
        left = right = null;
    }
    public int getFreq ()
    {
        return freq;
    }
}