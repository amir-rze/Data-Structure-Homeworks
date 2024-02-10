import javax.swing.text.Segment;
import java.util.*;

class Node {
    int count;
    Node left;
    Node right;

    public Node(int count, Node left, Node right) {
        this.count = count;
        this.left = left;
        this.right = right;
    }

    public Node insert(int l, int r, int k, Node firstNull) {
        if (l <= k && k < r) {
            if (l + 1 == r) {
                return new Node(this.count + 1, firstNull, firstNull);
            }
            int m = (l + r) / 2;
            return new Node(this.count + 1, this.left.insert(l, m, k, firstNull), this.right.insert(m, r, k, firstNull));
        }
        return this;
    }
}

class SegmentTree {
    private static int max = 50000;
    private Map<Integer, Integer> map;
    private int[] sortedArray;
    private int[] array;
    private Node[] nodes=new Node[max];
    private int count;
    Node firstNull = new Node(0, null, null);

    public SegmentTree(Map<Integer, Integer> map, int array[], int[] sortedArray, int count) {
        this.map = map;
        this.sortedArray = sortedArray;
        this.count = count;
        this.array=array;
        build();
    }

    public void build() {
        firstNull.left = firstNull.right = firstNull;
        for (int j = 0; j < array.length; j++) {
            nodes[j] = (j == 0 ? firstNull : nodes[j - 1]).insert(0, count, map.get(array[j]), firstNull);
        }
    }

    public int KthSmallest(int v, int u, int s, int count, int k) {
        int ans = kthSmallest(nodes[v], (u == 0 ? firstNull : nodes[u - 1]), 0, count, k);
        return sortedArray[ans];
    }

    public int kthSmallest(Node a, Node b, int l, int r, int k) {
        if (l + 1 == r) {
            return l;
        }
        int m = (l + r) / 2;
        int count = a.left.count - b.left.count;
        if (count >= k)
            return kthSmallest(a.left, b.left, l, m, k);
        return kthSmallest(a.right, b.right, m, r, k - count);
    }

}


public class Q4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int q = input.nextInt();
        int l, r, k;
        int[] array = new int[n];
        TreeSet<Integer> treeSet = new TreeSet<>();
        Map<Integer, Integer> map = new HashMap<>();
        int[] sortedArray;
        for (int i = 0; i < n; i++) {
            array[i] = input.nextInt();
            treeSet.add(array[i]);
        }
        sortedArray = new int[treeSet.size()];
        Iterator<Integer> it = treeSet.iterator();
        int i = 0;
        Integer current = null;
        while (it.hasNext() && i < treeSet.size()) {
            current = it.next();
            map.put(current, i);
            sortedArray[i] = current;
//            System.out.println(current);
            i++;
        }
        SegmentTree segmentTree = new SegmentTree(map, array, sortedArray, i);
        String order;
        for (int j = 0; j < q; j++) {
            order = input.next();
            if (order.equals("select")) {
                l = input.nextInt();
                r = input.nextInt();
                k = input.nextInt();
                l--;
                r--;
                System.out.println(segmentTree.KthSmallest(r, l, 0, i, k));
            }
        }
    }
}
