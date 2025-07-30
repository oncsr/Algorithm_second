import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static char[] start, end;
    private static int N;

    public static void main(String[] args) throws IOException {
        init();
        int answer = solve();

        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        N = Integer.parseInt(br.readLine());
        start = br.readLine().toCharArray();
        end = br.readLine().toCharArray();
    }

    private static int solve() {
        char[] temp1 = start.clone();
        int count1 = function(temp1, false);

        char[] temp2 = start.clone();
        int count2 = function(temp2, true);

        int answer = -1;
        if (count1 != -1 && count2 != -1) {
            answer = Math.min(count1, count2);
        } else if (count1 != -1) {
            answer = count1;
        } else if (count2 != -1) {
            answer = count2;
        }

        return answer;
    }

    private static int function(char[] arr, boolean pushFirst) {
        int count = 0;

        if (pushFirst) {
            pushSwitch(arr, 0);
            count++;
        }

        for (int i = 1; i < N; i++) {
            if (arr[i-1] != end[i-1]) {
                pushSwitch(arr, i);
                count++;
            }
        }

        if (arr[N-1] != end[N-1]) return -1;
        return count;
    }

    private static void pushSwitch(char[] arr, int index) {
        arr[index] = (arr[index] == '0') ? '1' : '0';

        if (index > 0) {
            arr[index-1] = (arr[index-1] == '0') ? '1' : '0';
        }

        if (index < N-1) {
            arr[index+1] = (arr[index+1] == '0') ? '1' : '0';
        }
    }
}