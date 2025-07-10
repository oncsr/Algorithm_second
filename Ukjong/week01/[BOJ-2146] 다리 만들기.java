
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
    private static List<int[]> list;        // 각 섬의 가장자리 좌표들을 저장하는 리스트
    private static int[][] map;             // 지도 배열 (0: 바다, 양수: 섬 번호)
    private static boolean[][] visited;     // BFS 탐색 시 방문 여부를 체크하는 배열
    private static boolean[][] checkBegin;  // 각 섬의 시작점이 이미 리스트에 추가되었는지 확인하는 배열
    private static int n, min;              // n: 맵 크기, min: 최단 다리 길이

    public static void main(String[] args) throws IOException {
        init();                    // 입력 받기 및 초기화
        checkIsland();             // 각 섬에 고유 번호 부여 및 시작점 찾기

        // 각 섬의 시작점에서 다른 섬까지의 최단 거리 구하기
        for (int[] element : list) {
            int result = BFS(element);
            min = Math.min(result, min);
        }

        // 결과 출력
        bw.write(min + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 초기화
    private static void init() throws IOException {
        n = Integer.parseInt(br.readLine());
        min = Integer.MAX_VALUE - 10;  // 최솟값을 저장할 변수 초기화
        map = new int[n][n];
        visited = new boolean[n][n];
        checkBegin = new boolean[n][n];
        list = new ArrayList<>();

        // 지도 정보 입력
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                // 섬(1)을 -1로 임시 변경 (나중에 섬 번호로 변경할 예정)
                if (map[i][j] == 1) map[i][j] = -1;
            }
        }
    }

    // 각 섬에 고유 번호를 부여하고 각 섬의 시작점(가장자리)을 찾는 메서드
    private static void checkIsland() {
        int num = 1;  // 섬 번호 (1부터 시작)
        visited = new boolean[n][n];

        // 모든 위치를 탐색하여 섬을 찾기
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == -1 && !visited[i][j]) {
                    // 새로운 섬을 발견했을 때 BFS로 전체 섬을 탐색
                    checkIsland(i, j, num, visited);
                    num++;  // 다음 섬 번호로 증가
                }
            }
        }

        resetVisited();  // 다음 작업을 위해 방문 배열 초기화
    }

    // 특정 섬의 모든 칸에 섬 번호를 부여하고, 다리 건설 시작점을 찾는 메서드
    private static void checkIsland(int x, int y, int num, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();
        visited[x][y] = true;
        q.add(new int[]{x, y});

        while (!q.isEmpty()) {
            int[] current = q.poll();
            map[current[0]][current[1]] = num;  // 현재 위치에 섬 번호 부여

            // 상하좌우 4방향 탐색
            for (int i = 0; i < 4; i++) {
                int nx = current[0] + dx[i];
                int ny = current[1] + dy[i];

                if (OOB(nx, ny) || visited[nx][ny]) continue;

                // 인접한 칸이 바다(0)인 경우
                if (map[nx][ny] == 0) {
                    // 해당 바다 칸이 아직 시작점 리스트에 추가되지 않았다면 추가
                    if (!checkBegin[nx][ny]) {
                        // {x좌표, y좌표, 출발하는 섬 번호}를 리스트에 추가
                        list.add(new int[]{nx, ny, num});
                        checkBegin[nx][ny] = true;
                    }
                    continue;
                }

                // 인접한 칸이 같은 섬(-1)인 경우 계속 탐색
                visited[nx][ny] = true;
                q.add(new int[]{nx, ny});
            }
        }
    }

    // 특정 시작점에서 다른 섬까지의 최단 거리를 구하는 BFS
    private static int BFS(int[] start) {
        Queue<int[]> q = new ArrayDeque<>();
        int result = 1;  // 다리 길이 (1부터 시작)

        visited[start[0]][start[1]] = true;
        // {x좌표, y좌표, 현재까지의 다리 길이}를 큐에 추가
        q.add(new int[]{start[0], start[1], result});

        while (!q.isEmpty()) {
            int[] current = q.poll();

            // 현재 위치가 바다가 아니고, 출발 섬과 다른 섬에 도달했다면
            if (map[current[0]][current[1]] != 0 && map[current[0]][current[1]] != start[2]) {
                result = current[2] - 1;  // 다리 길이 계산 (마지막 섬 칸 제외)
                break;
            }

            // 상하좌우 4방향 탐색
            for (int i = 0; i < 4; i++) {
                int nx = current[0] + dx[i];
                int ny = current[1] + dy[i];

                // 경계 체크, 출발 섬과 같은 섬인지 체크, 방문 여부 체크
                if (OOB(nx, ny) || map[nx][ny] == start[2] || visited[nx][ny]) continue;

                visited[nx][ny] = true;
                // 다음 위치와 증가된 거리를 큐에 추가
                q.add(new int[]{nx, ny, current[2] + 1});
            }
        }

        resetVisited();  // 다음 BFS를 위해 방문 배열 초기화
        return result;
    }

    // 방문 배열을 초기화하는 메서드
    private static void resetVisited() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                visited[i][j] = false;
            }
        }
    }

    // 경계를 벗어났는지 확인하는 메서드 (Out Of Bounds)
    private static boolean OOB(int nx, int ny) {
        return nx < 0 || nx > n - 1 || ny < 0 || ny > n - 1;
    }
}