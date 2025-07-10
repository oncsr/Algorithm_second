import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 상하좌우 이동을 위한 방향 배열
    private static final int[] dx = {1, 0, -1, 0};  // 하, 우, 상, 좌
    private static final int[] dy = {0, 1, 0, -1};

    // 전역 변수들
    private static int[][] map;        // 각 나라의 인구 수를 저장하는 배열
    private static boolean[][] visited; // BFS 탐색 시 방문 여부를 체크하는 배열
    private static int n, l, r, day;   // n: 맵 크기, l,r: 인구 차이 범위, day: 경과 일수

    public static void main(String[] args) throws IOException {
        init(); // 입력 받기 및 초기화
        day = 0;
        boolean flag = true; // 인구 이동이 발생했는지 확인하는 플래그

        // 더 이상 인구 이동이 없을 때까지 반복
        while (flag) {
            int count = 0; // 하루 동안 발생한 인구 이동 연합의 개수

            // 모든 위치에서 BFS 수행
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (!visited[i][j]) { // 아직 방문하지 않은 위치라면
                        count += BFS(i, j); // BFS를 통해 연합 찾기
                    }
                }
            }

            // 인구 이동이 발생했다면 일수 증가
            if (count > 0) day++;
            else flag = false; // 인구 이동이 없으면 종료

            resetVisited(); // 다음 날을 위해 방문 배열 초기화
        }

        // 결과 출력
        bw.write(day + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 초기화
    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken()); // 맵 크기
        l = Integer.parseInt(st.nextToken()); // 인구 차이 최소값
        r = Integer.parseInt(st.nextToken()); // 인구 차이 최대값

        map = new int[n][n];
        visited = new boolean[n][n];

        // 각 나라의 인구 수 입력
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }

    // BFS를 통해 연합을 찾고 인구 이동 처리
    private static int BFS(int x, int y) {
        Queue<int[]> q = new ArrayDeque<>();
        List<int[]> list = new ArrayList<>(); // 연합에 속한 나라들의 좌표 저장
        int count = 0;      // 연합에 속한 나라의 개수
        int population = 0; // 연합의 총 인구 수

        visited[x][y] = true;
        q.add(new int[]{x, y});

        while (!q.isEmpty()) {
            int[] current = q.poll();
            count++;
            population += map[current[0]][current[1]];
            list.add(current);

            // 상하좌우 4방향 탐색
            for (int i = 0; i < 4; i++) {
                int nx = current[0] + dx[i];
                int ny = current[1] + dy[i];

                // 경계 체크, 인구 차이 조건 체크, 방문 여부 체크
                if (OOB(nx, ny) || !valid(map[current[0]][current[1]], map[nx][ny]) || visited[nx][ny]) continue;

                visited[nx][ny] = true;
                q.add(new int[]{nx, ny});
            }
        }

        // 연합에 속한 모든 나라의 인구를 평균값으로 변경
        for (int[] element : list) {
            map[element[0]][element[1]] = population / count;
        }

        // 연합이 2개 이상의 나라로 구성되어야 인구 이동 발생
        return count > 1 ? 1 : 0;
    }

    // 다음 날을 위해 방문 배열 초기화
    private static void resetVisited() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                visited[i][j] = false;
            }
        }
    }

    // 두 나라의 인구 차이가 L 이상 R 이하인지 확인
    private static boolean valid(int a, int b) {
        int num = Math.abs(a - b);
        return num <= r && num >= l;
    }

    // 경계를 벗어났는지 확인 (Out Of Bounds)
    private static boolean OOB(int nx, int ny) {
        return nx < 0 || nx > n - 1 || ny < 0 || ny > n - 1;
    }
}