import java.util.Arrays;
import java.util.Scanner;

public class Q4 {

    public static int partition(int[] arr, int l, int r) {
        int x = arr[r], i = l;
        for (int j = l; j <= r - 1; j++) {
            if (arr[j] <= x) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
            }
        }
        int temp = arr[i];
        arr[i] = arr[r];
        arr[r] = temp;
        return i;
    }

    public static int kthSmallest(int[] arr, int l, int r, int k) {
        if (k > 0 && k <= r - l + 1) {
            int pos = partition(arr, l, r);
            if (pos - l == k - 1)
                return arr[pos];
            if (pos - l > k - 1)
                return kthSmallest(arr, l, pos - 1, k);
            return kthSmallest(arr, pos + 1, r, k - pos + l - 1);
        }
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int q = input.nextInt();
        int l, r, k;
        int[] array = new int[n];
        int [] newArray ;
        for (int i = 0; i < n; i++) {
            array[i] = input.nextInt();
        }
        String order;
        for (int i = 0; i < q; i++) {
            order = input.next();
            if (order.equals("select")) {
                l = input.nextInt();
                r = input.nextInt();
                k = input.nextInt();
                newArray = Arrays.copyOfRange(array, l - 1, r);
                System.out.println(kthSmallest(newArray,0,newArray.length-1, k));
            }
        }
    }
}
