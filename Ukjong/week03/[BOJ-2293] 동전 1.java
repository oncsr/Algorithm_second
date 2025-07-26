import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    private static int[] dp;  // dp[i] = 금액 i를 만드는 경우의 수
    private static int N, K;  // N: 동전 종류의 개수, K: 목표 금액

    public static void main(String[] args) throws IOException {
        init();  // 입력 처리 및 DP 계산
        bw.write(dp[K] + "\n");  // 목표 금액 K를 만드는 경우의 수 출력
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 처리 및 동적 프로그래밍으로 경우의 수 계산
    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());  // 동전의 종류 개수
        K = Integer.parseInt(st.nextToken());  // 만들려는 목표 금액

        // DP 배열 초기화
        // dp[i] = 금액 i를 만드는 방법의 수
        dp = new int[K + 1];

        // 각 동전 종류에 대해 DP 테이블 업데이트
        for (int i = 0; i < N; i++) {
            int coin = Integer.parseInt(br.readLine());  // 현재 동전의 가치

            // 금액 0부터 K까지 각각에 대해 현재 동전을 사용했을 때의 경우의 수 계산
            for (int j = 0; j <= K; j++) {
                if (j == 0) {
                    // 기저 조건: 금액 0을 만드는 방법은 항상 1가지 (아무 동전도 사용하지 않기)
                    dp[j] = 1;
                } else if (j >= coin) {
                    // 현재 금액 j가 동전의 가치 이상인 경우
                    // 현재 동전을 사용해서 금액 j를 만들 수 있음
                    //
                    // dp[j] += dp[j - coin]의 의미:
                    // - 기존에 금액 j를 만드는 방법의 수: dp[j]
                    // - 현재 동전을 하나 사용해서 금액 j를 만드는 방법의 수: dp[j - coin]
                    // - 이 둘을 합쳐서 총 경우의 수 갱신
                    dp[j] += dp[j - coin];
                }
                // j < coin인 경우: 현재 동전으로는 금액 j를 만들 수 없으므로 변화 없음
            }
        }
    }
}