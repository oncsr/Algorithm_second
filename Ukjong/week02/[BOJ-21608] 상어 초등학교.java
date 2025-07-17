import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static final int[] dx = {1, 0, -1, 0};
    private static final int[] dy = {0, 1, 0, -1};
    private static Set<Integer>[] set;
    private static int[][] map, like;
    private static int n, answer;
    public static void main(String[] args) throws IOException {
        init();
        for (int i = 1; i <= n*n; i++) {
            int[] pos = select(like[i]);
            map[pos[0]][pos[1]] = like[i][0];
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int score = getScore(i, j);
                answer += score;
            }
        }

        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        n = Integer.parseInt(br.readLine());
        map = new int[n + 1][n + 1];
        set = new Set[n*n + 1];
        like = new int[n*n + 1][5];

        for (int i = 1; i <= n*n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());

            like[i][0] = a;
            like[i][1] = b;
            like[i][2] = c;
            like[i][3] = d;
            like[i][4] = e;
            set[i] = new HashSet<>();
        }

        for (int i = 1; i <= n*n; i++) {
            for (int j = 1; j <= 4; j++)
                set[like[i][0]].add(like[i][j]);
        }
    }

    private static int[] select(int[] arr) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) return Integer.compare(o2[0], o1[0]);
            if (o1[1] != o2[1]) return Integer.compare(o2[1], o1[1]);
            if (o1[2] != o2[2]) return Integer.compare(o1[2], o2[2]);
            return Integer.compare(o1[3], o2[3]);
        });
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (map[i][j] != 0) continue;

                int f = 0, b = 0;
                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k], ny = j + dy[k];
                    if (OOB(nx, ny)) continue;
                    if (set[arr[0]].contains(map[nx][ny])) f++;
                    if (map[nx][ny] == 0) b++;
                }
                pq.add(new int[]{f, b, i, j});
            }
        }

        int[] result = pq.poll();
        return new int[]{result[2], result[3]};
    }

    private static int getScore(int i, int j) {
        int score = 0;
        int count = 0;
        for (int k = 0; k < 4; k++) {
            int nx = i + dx[k];
            int ny = j + dy[k];

            if (OOB(nx, ny)) continue;
            if (set[map[i][j]].contains(map[nx][ny])) count++;
        }

        if (count > 0) {
            score = 1;
            for (int k = 1; k < count; k++) {
                score *= 10;
            }
        }
        return score;
    }

    private static boolean OOB(int nx, int ny) {
        return nx < 1 || nx > n || ny < 1 || ny > n;
    }
}