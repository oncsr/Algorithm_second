import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 전역 변수들
    private static int[] arr;           // arr[i] = i번 학생이 선택한 학생 번호
    private static boolean[] visited;   // DFS 탐색 과정에서 방문한 노드를 체크
    private static boolean[] finished;  // DFS가 완전히 끝난 노드를 체크 (백트래킹 시 사용)
    private static int n, count;        // n: 학생 수, count: 팀을 이룬 학생 수

    public static void main(String[] args) throws IOException {
        int t = Integer.parseInt(br.readLine());  // 테스트 케이스 개수

        // 각 테스트 케이스마다 실행
        while (t-- > 0) {
            init();  // 입력 받기 및 초기화

            // 모든 학생에 대해 DFS 수행
            for (int i = 1; i <= n; i++) {
                if (visited[i]) continue;  // 이미 처리된 학생은 건너뛰기
                DFS(i);
            }

            // 팀을 이루지 못한 학생 수 = 전체 학생 수 - 팀을 이룬 학생 수
            bw.write((n - count) + "\n");
        }

        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 초기화
    private static void init() throws IOException {
        n = Integer.parseInt(br.readLine());
        count = 0;  // 팀을 이룬 학생 수 초기화

        arr = new int[n + 1];
        visited = new boolean[n + 1];
        finished = new boolean[n + 1];

        StringTokenizer st = new StringTokenizer(br.readLine());
        // 각 학생이 선택한 학생 번호 입력
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
    }

    /**
     * DFS를 통해 사이클을 탐지하는 핵심 메서드
     *
     * 이 문제의 핵심: 각 학생은 정확히 한 명을 선택하므로,
     * 어떤 학생에서 시작하더라도 결국 사이클을 만나게 됨
     *
     * 사이클을 이루는 학생들만이 팀을 구성할 수 있음
     */
    private static void DFS(int node) {
        visited[node] = true;  // 현재 노드를 방문 처리
        int next = arr[node];  // 현재 학생이 선택한 다음 학생

        if (!visited[next]) {
            // 다음 학생이 아직 방문되지 않았다면 계속 탐색
            DFS(next);
        } else if (!finished[next]) {
            // 다음 학생이 방문되었지만 아직 완료되지 않았다면 사이클 발견!
            // finished[next] == false 는 next가 현재 DFS 경로상에 있다는 의미

            int current = next;  // 사이클의 시작점부터
            do {
                count++;  // 사이클에 포함된 학생 수 증가
                current = arr[current];  // 사이클을 따라 이동
            } while (current != next);  // 사이클의 시작점으로 돌아올 때까지
        }
        // else: visited[next] == true && finished[next] == true
        // 이미 다른 DFS에서 처리가 완료된 노드이므로 아무것도 하지 않음

        finished[node] = true;  // 현재 노드의 DFS 완료 처리
    }
}