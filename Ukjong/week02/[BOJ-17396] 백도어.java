import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static final long INF = 30000000000L;
    private static List<Node>[] graph;
    private static long[] dist;
    private static boolean[] canVisit;
    private static int n, m;
    public static void main(String[] args) throws IOException {
        init();
        dijkstra(0);

        long answer = dist[n - 1] != INF ? dist[n - 1] : -1;
        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        canVisit = new boolean[n];
        graph = new List[n];
        dist = new long[n];

        Arrays.fill(dist, INF);

        for (int i = 0; i < n; i++) {
            if (Integer.parseInt(st.nextToken()) == 0) canVisit[i] = true;
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());

            graph[u].add(new Node(v, w));
            graph[v].add(new Node(u, w));
        }
    }


    private static void dijkstra(int start) {
        PriorityQueue<Node> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.dest, o2.dest));
        dist[start] = 0;
        pq.add(new Node(start, dist[start]));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.cost > dist[current.dest]) continue;

            for (Node next : graph[current.dest]) {
                if (!canVisit[next.dest] && next.dest != n - 1) continue;
                long nCost = current.cost + next.cost;
                if (nCost < dist[next.dest]) {
                    dist[next.dest] = nCost;
                    pq.add(new Node(next.dest, dist[next.dest]));
                }
            }
        }
    }

    static class Node {
        int dest;
        long cost;

        public Node(int dest, long cost) {
            this.dest = dest;
            this.cost = cost;
        }
    }
}