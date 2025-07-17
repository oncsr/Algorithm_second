import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static boolean[][] canCut;
    private static int[][] dp;
    private static int m, n, answer;
    public static void main(String[] args) throws IOException {
        init();
        DP();

        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        m = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        dp = new int[m + 1][n + 1];
        canCut = new boolean[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= n; j++) {
                if (Integer.parseInt(st.nextToken()) == 0) canCut[i][j] = true;
            }
        }
    }

    private static void DP() {
        for (int i = 1; i <= m; i++) {
            if (canCut[i][1]) {
                dp[i][1] = 1;
                answer = Math.max(answer, 1);
            }
        }

        for (int i = 1; i <= n; i++) {
            if (canCut[1][i]) {
                dp[1][i] = 1;
                answer = Math.max(answer, 1);
            }
        }

        for (int i = 2; i <= m; i++) {
            for (int j = 2; j <= n; j++) {
                if (canCut[i][j]) {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                    answer = Math.max(answer, dp[i][j]);
                }
            }
        }
    }
}