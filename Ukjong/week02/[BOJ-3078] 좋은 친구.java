import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static int n, k;
    private static long answer;
    public static void main(String[] args) throws IOException {
        init();

        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        answer = 0;

        Queue<Integer> q = new ArrayDeque<>();
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int name = br.readLine().length();

            if (q.size() > k) {
                int num = q.poll();
                answer += map.get(num) - 1;
                map.put(num, map.get(num) - 1);
                if (map.get(num) == 0) map.remove(num);
            }

            q.add(name);
            map.put(name, map.getOrDefault(name, 0) + 1);
        }

        while (!q.isEmpty()) {
            int num = q.poll();
            answer += map.get(num) - 1;
            map.put(num, map.get(num) - 1);
            if (map.get(num) == 0) map.remove(num);
        }
    }
}