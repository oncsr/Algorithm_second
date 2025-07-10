import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 전역 변수들
    private static PriorityQueue<Integer> dist;  // 센서들 간의 거리를 저장하는 최대힙
    private static int[] sensor;                 // 센서들의 좌표를 저장하는 배열
    private static int n, k;                     // n: 센서 개수, k: 집중국 개수

    public static void main(String[] args) throws IOException {
        init();           // 입력 받기 및 초기화
        int answer = greedy();  // 그리디 알고리즘으로 답 계산

        // 결과 출력
        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 초기화
    private static void init() throws IOException {
        n = Integer.parseInt(br.readLine());  // 센서 개수
        k = Integer.parseInt(br.readLine());  // 집중국 개수

        StringTokenizer st = new StringTokenizer(br.readLine());
        sensor = new int[n];

        // 최대힙 생성 (가장 큰 거리부터 poll되도록)
        dist = new PriorityQueue<>((o1, o2) -> Integer.compare(o2, o1));

        // 센서 좌표 입력
        for (int i = 0; i < n; i++) {
            sensor[i] = Integer.parseInt(st.nextToken());
        }

        // 센서 좌표를 오름차순으로 정렬
        Arrays.sort(sensor);

        // 인접한 센서들 간의 거리를 계산하여 우선순위 큐에 저장
        for (int i = 0; i < n - 1; i++) {
            dist.add(sensor[i + 1] - sensor[i]);
        }
    }

    // 그리디 알고리즘으로 최소 수신 가능 영역의 길이의 합 계산
    private static int greedy() {
        int result = 0;

        /*
         * 핵심 아이디어:
         * - k개의 집중국을 설치하면 센서들을 k개의 그룹으로 나눌 수 있음
         * - 각 그룹은 연속된 센서들로 구성되어야 함
         * - 그룹을 나누는 방법: 센서들 간의 거리가 가장 큰 (k-1)개의 간격을 끊어내기
         * - 가장 큰 간격들을 제거하면 남은 간격들의 합이 최소가 됨
         */

        // 가장 큰 (k-1)개의 거리를 제거
        for (int i = 0; i < k - 1; i++) {
            dist.poll();  // 최대힙에서 가장 큰 거리 제거
        }

        // 남은 거리들의 합이 답
        while (!dist.isEmpty()) {
            result += dist.poll();
        }

        return result;
    }
}