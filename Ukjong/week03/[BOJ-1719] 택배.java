import java.util.*;
import java.io.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    private static final int INF = 10000010;  // 무한대 값 (충분히 큰 수)
    private static List<Node>[] graph;        // 인접 리스트로 그래프 표현
    private static int[] dist;                // 최단 거리 배열
    private static int[][] path;              // 경로 테이블 (i에서 j로 가는 첫 번째 경유지)
    private static int N, M;                  // N: 집하장 개수, M: 간선 개수

    public static void main(String[] args) throws IOException {
        init();  // 초기화

        // 모든 집하장에서 다른 모든 집하장으로의 최단 경로 계산
        for (int i = 1; i <= N; i++) {
            dijkstra(i);  // i번 집하장을 시작점으로 다익스트라 실행
        }

        // 경로 테이블 출력
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (path[i][j] == 0) {
                    bw.write("-" + " ");  // 자기 자신으로 가는 경우는 "-"
                }
                else {
                    bw.write(path[i][j] + " ");  // i에서 j로 가는 첫 번째 경유지
                }
            }
            bw.write("\n");
        }

        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 초기화
    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());  // 집하장 개수
        M = Integer.parseInt(st.nextToken());  // 간선 개수

        // 배열 및 리스트 초기화
        graph = new List[N + 1];  // 1번부터 N번까지 사용
        dist = new int[N + 1];    // 최단 거리 배열
        path = new int[N + 1][N + 1];  // 경로 테이블

        // 각 집하장의 인접 리스트 초기화
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        // 간선 정보 입력
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());  // 시작 집하장
            int v = Integer.parseInt(st.nextToken());  // 도착 집하장
            int w = Integer.parseInt(st.nextToken());  // 비용

            // 양방향 간선 추가 (무방향 그래프)
            graph[u].add(new Node(v, w));
            graph[v].add(new Node(u, w));
        }
    }

    // 다익스트라 알고리즘으로 start에서 모든 정점까지의 최단 경로 계산
    private static void dijkstra(int start) {
        // 비용 기준으로 최소 힙 사용
        PriorityQueue<Node> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));

        // 거리 배열 초기화 (무한대로 설정)
        Arrays.fill(dist, INF);
        dist[start] = 0;  // 시작점의 거리는 0

        // 시작점을 우선순위 큐에 추가
        pq.add(new Node(start, dist[start]));

        while (!pq.isEmpty()) {
            Node current = pq.poll();  // 현재 가장 비용이 적은 노드

            // 이미 처리된 노드라면 건너뛰기 (중복 처리 방지)
            if (current.cost > dist[current.dest]) continue;

            // 현재 노드와 연결된 모든 인접 노드 확인
            for (Node next : graph[current.dest]) {
                int nCost = current.cost + next.cost;  // 새로운 경로의 비용

                // 더 짧은 경로를 발견했다면
                if (nCost < dist[next.dest]) {
                    dist[next.dest] = nCost;  // 최단 거리 갱신

                    // 경로 추적: 시작점에서 목적지로 가는 첫 번째 경유지 저장
                    if (current.path == 0) {
                        // 시작점에서 직접 연결된 경우
                        // start -> next.dest로 바로 가는 경우
                        path[start][next.dest] = next.dest;
                        pq.add(new Node(next.dest, nCost, next.dest));
                    }
                    else {
                        // 경유지를 거쳐서 가는 경우
                        // start -> current.path -> ... -> next.dest
                        // 첫 번째 경유지는 current.path와 동일
                        path[start][next.dest] = current.path;
                        pq.add(new Node(next.dest, nCost, current.path));
                    }
                }
            }
        }
    }

    // 노드 클래스: 목적지, 비용, 경로 정보를 담음
    static class Node{
        int dest;  // 목적지 집하장 번호
        int cost;  // 해당 목적지까지의 비용
        int path;  // 시작점에서 이 노드로 가는 첫 번째 경유지

        // 기본 생성자: 경로 정보 없이 노드 생성 (그래프 구성용)
        public Node(int dest, int cost) {
            this.dest = dest;
            this.cost = cost;
            this.path = 0;  // 경로 정보 없음
        }

        // 경로 정보 포함 생성자: 다익스트라 실행 중 사용
        public Node(int dest, int cost, int path) {
            this.dest = dest;
            this.cost = cost;
            this.path = path;  // 첫 번째 경유지 정보
        }
    }
}