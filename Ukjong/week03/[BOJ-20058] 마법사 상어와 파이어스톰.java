import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 상하좌우 이동을 위한 방향 배열
    private static int[] dx = {1, 0, -1, 0};  // 하, 우, 상, 좌
    private static int[] dy = {0, 1, 0, -1};

    private static int[][] arr;           // 얼음의 양을 저장하는 2차원 배열
    private static boolean[][] visited;   // BFS에서 방문 체크용 배열
    private static int N, Q, length;      // N: 격자 크기 지수, Q: 파이어스톰 시전 횟수, length: 실제 격자 크기(2^N)

    public static void main(String[] args) throws IOException {
        init();  // 입력 처리 및 파이어스톰 시전

        // 결과 계산
        int totalIce = countIce();        // 남은 얼음의 총합
        int largestIce = 0;               // 가장 큰 얼음 덩어리의 크기

        // 모든 칸을 확인하여 가장 큰 연결된 얼음 덩어리 찾기
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                // 얼음이 있고 아직 방문하지 않은 칸이면 BFS 시작
                if (arr[i][j] > 0 && !visited[i][j]) {
                    int n = BFS(i, j);  // 해당 덩어리의 크기 계산
                    largestIce = Math.max(largestIce, n);
                }
            }
        }

        // 결과 출력: 총 얼음의 양, 가장 큰 덩어리의 크기
        bw.write(totalIce + "\n" + largestIce + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 처리 및 초기화, 파이어스톰 시전
    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());  // 격자 크기는 2^N × 2^N
        Q = Integer.parseInt(st.nextToken());  // 파이어스톰 시전 횟수
        length = (int) Math.pow(2, N);         // 실제 격자 크기 계산

        // 배열 초기화
        arr = new int[length][length];         // 얼음 배열
        visited = new boolean[length][length]; // 방문 체크 배열

        // 초기 얼음 상태 입력
        for (int i = 0; i < length; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < length; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 파이어스톰 시전
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < Q; i++) {
            int l = (int) Math.pow(2, Integer.parseInt(st.nextToken()));  // 2^L 크기의 부분 격자
            rotate(l);  // 1단계: 부분 격자들을 시계방향으로 90도 회전
            melt();     // 2단계: 얼음 녹이기
        }
    }

    // 1단계: 격자를 L×L 크기의 부분 격자로 나누어 각각을 시계방향 90도 회전
    private static void rotate(int L) {
        // 격자를 L×L 크기로 나누어 각 부분격자마다 회전 수행
        for (int i = 0; i < length; i += L) {
            for (int j = 0; j < length; j += L) {
                rotate(i, j, L);  // (i,j)를 왼쪽 위 모서리로 하는 L×L 격자 회전
            }
        }
    }

    // 특정 위치 (x, y)에서 시작하는 L×L 부분격자를 시계방향 90도 회전
    private static void rotate(int x, int y, int L) {
        int[][] temp = new int[L][L];  // 임시 배열

        // 시계방향 90도 회전: (i, j) → (j, L-1-i)
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                temp[j][L - 1 - i] = arr[x + i][y + j];
            }
        }

        // 회전된 결과를 원래 배열에 복사
        for (int i = x; i < x + L; i++) {
            for (int j = y; j < y + L; j++) {
                arr[i][j] = temp[i - x][j - y];
            }
        }
    }

    // 2단계: 얼음 녹이기 - 인접한 칸 중 얼음이 있는 칸이 2개 이하면 얼음 1 감소
    private static void melt() {
        List<int[]> temp = new ArrayList<>();  // 녹을 얼음의 위치를 저장

        // 모든 칸을 확인하여 녹을 얼음 찾기
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int count = 0;  // 인접한 칸 중 얼음이 없거나 범위를 벗어난 칸의 개수

                // 상하좌우 4방향 확인
                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];

                    // 범위를 벗어나거나 얼음이 없는 칸이면 count 증가
                    if (OOB(nx, ny) || arr[nx][ny] == 0) {
                        count++;
                    }
                }

                // 얼음이 있는 칸 중에서 인접한 얼음이 있는 칸이 2개 이하인 경우
                // (즉, 얼음이 없거나 범위를 벗어난 칸이 2개 이상인 경우)
                if (count > 1 && arr[i][j] > 0) {
                    temp.add(new int[]{i, j});  // 녹을 위치 저장
                }
            }
        }

        // 저장된 위치의 얼음을 1씩 감소
        for (int[] element : temp) {
            arr[element[0]][element[1]]--;
        }
    }

    // 남은 얼음의 총합 계산
    private static int countIce() {
        int result = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                result += arr[i][j];
            }
        }
        return result;
    }

    // BFS를 사용하여 연결된 얼음 덩어리의 크기 계산
    private static int BFS(int x, int y) {
        Queue<int[]> q = new ArrayDeque<>();
        int result = 1;           // 현재 위치도 포함하므로 1부터 시작
        visited[x][y] = true;     // 시작 위치 방문 처리
        q.add(new int[]{x, y});   // 큐에 시작 위치 추가

        while (!q.isEmpty()) {
            int[] current = q.poll();  // 현재 위치

            // 상하좌우 4방향 탐색
            for (int i = 0; i < 4; i++) {
                int nx = current[0] + dx[i];
                int ny = current[1] + dy[i];

                // 범위를 벗어나거나, 얼음이 없거나, 이미 방문한 경우 건너뛰기
                if (OOB(nx, ny) || arr[nx][ny] == 0 || visited[nx][ny]) continue;

                // 새로운 얼음 발견
                visited[nx][ny] = true;  // 방문 처리
                result++;                // 덩어리 크기 증가
                q.add(new int[]{nx, ny}); // 큐에 추가하여 계속 탐색
            }
        }

        return result;  // 연결된 얼음 덩어리의 크기 반환
    }

    // 좌표가 격자 범위를 벗어났는지 확인하는 함수
    private static boolean OOB(int nx, int ny) {
        return nx < 0 || nx > length - 1 || ny < 0 || ny > length - 1;
    }
}