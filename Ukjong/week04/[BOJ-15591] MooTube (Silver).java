import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static List<Node>[] graph;
    private static boolean[] visited;
    private static int N, Q;

    public static void main(String[] args) throws IOException {
        init();
        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int K = Integer.parseInt(st.nextToken());
            int V = Integer.parseInt(st.nextToken());

            int answer = BFS(K, V);
            bw.write(answer + "\n");
        }

        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        graph = new List[N + 1];
        visited = new boolean[N + 1];

        for (int i = 0; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 1; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());

            graph[u].add(new Node(v, w));
            graph[v].add(new Node(u, w));
        }
    }

    private static int BFS(int K, int V) {
        Queue<Integer> q = new ArrayDeque<>();
        int count = 0;
        Arrays.fill(visited, false);
        visited[V] = true;
        q.add(V);

        while (!q.isEmpty()) {
            int current = q.poll();

            for (Node next : graph[current]) {
                if (next.cost < K || visited[next.dest]) continue;
                count++;
                visited[next.dest] = true;
                q.add(next.dest);
            }
        }

        return count;
    }

    static class Node {
        int dest;
        int cost;

        public Node(int dest, int cost) {
            this.dest = dest;
            this.cost = cost;
        }
    }
}