import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static final int MOD = 10007;
    private static int[] DP;
    private static List<Integer>[] blocks;
    private static int N, M, H;

    public static void main(String[] args) throws IOException {
        init();
        DP();

        bw.write(DP[H] + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        blocks = new List[N + 1];
        DP = new int[H + 1];
        DP[0] = 1;

        for (int i = 1; i <= N; i++) {
            blocks[i] = new ArrayList<>();
            st = new StringTokenizer(br.readLine());
            while (st.hasMoreTokens()) {
                blocks[i].add(Integer.parseInt(st.nextToken()));
            }
            blocks[i].add(0);
        }
    }

    private static void DP() {
        for (int i = 1; i <= N; i++) {
            int[] temp = new int[H + 1];
            for (int block : blocks[i]) {
                for (int h = block; h <= H; h++) {
                    temp[h] = (temp[h] + DP[h - block]) % MOD;
                }
            }
            DP = temp;
        }
    }
}