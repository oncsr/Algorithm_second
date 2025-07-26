import java.util.*;
import java.io.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 상하좌우 이동을 위한 방향 배열
    private static final int[] dx = {1, 0, -1, 0};  // 하, 우, 상, 좌
    private static final int[] dy = {0, 1, 0, -1};

    private static char[][] map;        // 방의 지도
    private static List<int[]> dust;    // 먼지 위치들을 저장하는 리스트
    private static boolean[][][] visited;  // 방문 체크 배열 [x][y][먼지수집상태]
    private static int[] start;         // 로봇 청소기의 시작 위치
    private static int w, h;            // 방의 너비와 높이

    public static void main(String[] args) throws IOException {
        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            w = Integer.parseInt(st.nextToken());  // 너비
            h = Integer.parseInt(st.nextToken());  // 높이

            // 0 0 입력시 프로그램 종료
            if (w == 0 && h == 0) break;

            init();  // 초기화
            int answer = BFS();  // BFS로 최소 이동 횟수 계산
            bw.write(answer + "\n");
        }
        bw.flush();
        bw.close();
        br.close();
    }

    // 초기화 함수
    public static void init() throws IOException {
        start = new int[2];
        map = new char[h][w];
        dust = new ArrayList<>();

        // 지도 입력 및 시작점, 먼지 위치 찾기
        for (int i = 0; i < h; i++) {
            String input = br.readLine();
            for (int j = 0; j < w; j++) {
                map[i][j] = input.charAt(j);

                // 로봇 청소기 시작 위치
                if (map[i][j] == 'o') {
                    start[0] = i;
                    start[1] = j;
                }

                // 먼지 위치를 리스트에 저장
                if (map[i][j] == '*') {
                    dust.add(new int[]{i, j});
                }
            }
        }

        // 3차원 방문 배열 초기화
        // [x좌표][y좌표][먼지 수집 상태를 비트마스크로 표현]
        visited = new boolean[h][w][1 << dust.size()];
    }

    // BFS를 이용한 최단 경로 탐색
    private static int BFS() {
        Queue<int[]> q = new ArrayDeque<>();

        // 시작 위치와 먼지 수집 상태 0으로 초기화
        visited[start[0]][start[1]][0] = true;
        q.add(new int[]{start[0], start[1], 0, 0});  // {x, y, 먼지수집상태, 이동횟수}

        while (!q.isEmpty()) {
            int[] current = q.poll();

            // 모든 먼지를 수집했다면 현재까지의 이동 횟수 반환
            // (1 << dust.size()) - 1 은 모든 먼지를 수집한 상태의 비트마스크
            if (current[2] == ((1 << dust.size()) - 1)) {
                return current[3];
            }

            // 4방향으로 이동 시도
            for (int i = 0; i < 4; i++) {
                int nx = current[0] + dx[i];  // 다음 x 좌표
                int ny = current[1] + dy[i];  // 다음 y 좌표
                int nMask = current[2];       // 현재 먼지 수집 상태 복사

                // 범위를 벗어나거나 가구가 있는 곳은 이동 불가
                if (OOB(nx, ny) || map[nx][ny] == 'x') continue;

                // 먼지가 있는 위치에 도달했다면
                if (map[nx][ny] == '*') {
                    int dustIndex = getDustIndex(nx, ny);  // 해당 먼지의 인덱스 찾기
                    nMask |= (1 << dustIndex);  // 비트마스크에 해당 먼지 수집 표시
                }

                // 해당 위치와 먼지 수집 상태로 방문한 적이 없다면
                if (!visited[nx][ny][nMask]) {
                    visited[nx][ny][nMask] = true;
                    q.add(new int[]{nx, ny, nMask, current[3] + 1});
                }
            }
        }

        // 모든 먼지를 수집할 수 없는 경우
        return -1;
    }

    // 좌표가 범위를 벗어났는지 확인하는 함수
    private static boolean OOB(int nx, int ny) {
        return nx < 0 || nx > h - 1 || ny < 0 || ny > w - 1;
    }

    // 주어진 좌표의 먼지가 리스트에서 몇 번째 인덱스인지 찾는 함수
    private static int getDustIndex(int nx, int ny) {
        for (int i = 0; i < dust.size(); i++) {
            if (nx == dust.get(i)[0] && ny == dust.get(i)[1]) {
                return i;
            }
        }
        return -1;  // 찾지 못한 경우 (실제로는 발생하지 않음)
    }
}