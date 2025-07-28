import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    private static int[] uf;    // Union-Find의 부모 배열
    private static int[] size;  // 각 집합의 크기 (Union by Size 최적화용)
    private static int N, M, answer;  // N: 뉴런 개수, M: 시냅스 개수, answer: 최소 수술 횟수

    public static void main(String[] args) throws IOException {
        init();  // 입력 처리 및 계산
        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 처리 및 핵심 로직
    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());  // 뉴런의 개수
        M = Integer.parseInt(st.nextToken());  // 시냅스의 개수

        // Union-Find 자료구조 초기화
        uf = new int[N + 1];    // 부모 배열
        size = new int[N + 1];  // 각 집합의 원소 개수

        // 초기화: 각 뉴런을 독립적인 집합으로 설정
        for (int i = 1; i <= N; i++) {
            uf[i] = i;      // 자기 자신을 부모로 설정
            size[i] = 1;    // 초기 집합 크기는 1
        }

        int cycleEdges = 0;  // 사이클을 형성하는 간선의 개수 (제거해야 할 간선)
        int validEdges = 0;  // 트리 구조에 포함되는 유효한 간선의 개수

        // 모든 시냅스(간선) 처리
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());  // 시냅스로 연결된 뉴런1
            int v = Integer.parseInt(st.nextToken());  // 시냅스로 연결된 뉴런2

            // 두 뉴런의 루트(대표) 찾기
            int U = find(u);
            int V = find(v);

            if (U == V) {
                // 같은 집합에 속한 뉴런들을 연결하는 간선
                // → 사이클을 형성하므로 제거해야 함
                cycleEdges++;
            } else {
                // 다른 집합에 속한 뉴런들을 연결하는 간선
                // → 두 집합을 합치고, 트리 구조에 포함됨
                union(u, v);
                validEdges++;
            }
        }

        // 연결된 컴포넌트(집합)의 개수 계산
        Set<Integer> roots = new HashSet<>();
        for (int i = 1; i <= N; i++) {
            roots.add(find(i));  // 각 뉴런의 루트를 Set에 추가
        }

        // 최소 수술 횟수 계산
        // = 제거할 간선 수 + 추가할 간선 수
        // = cycleEdges + (컴포넌트 개수 - 1)
        answer = cycleEdges + roots.size() - 1;
    }

    // Union 연산: 두 집합을 합치기 (Union by Size 최적화)
    private static void union(int x, int y) {
        int X = find(x);  // x의 루트 찾기
        int Y = find(y);  // y의 루트 찾기

        // 크기가 작은 트리를 큰 트리 아래에 붙이기 (Union by Size)
        if (size[X] < size[Y]) {
            uf[X] = Y;          // X의 부모를 Y로 설정
            size[Y] += size[X]; // Y 집합의 크기 증가
        } else {
            uf[Y] = X;          // Y의 부모를 X로 설정
            size[X] += size[Y]; // X 집합의 크기 증가
        }
    }

    // Find 연산: 원소 x가 속한 집합의 루트 찾기 (경로 압축 최적화)
    private static int find(int x) {
        if (uf[x] == x) return x;  // 루트 노드인 경우

        // 경로 압축: 재귀적으로 루트를 찾으면서
        // 중간 노드들의 부모를 루트로 직접 연결
        return uf[x] = find(uf[x]);
    }
}