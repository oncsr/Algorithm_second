import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 사용 가능한 연산자들 (*, +, /) - 문제에서 사전순으로 가장 앞서는 것을 출력해야 하므로 순서 중요
    private static final char[] oper = {'*', '+', '/'};

    // 전역 변수들
    private static Set<Long> visited;  // 이미 방문한 숫자들을 저장하는 Set (중복 방지)
    private static long s, t;          // s: 시작 숫자, t: 목표 숫자
    private static String result;      // 최종 결과 문자열

    public static void main(String[] args) throws IOException {
        init();  // 입력 받기 및 초기화

        // 결과 출력
        bw.write(result + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 초기화
    private static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        s = Integer.parseInt(st.nextToken());  // 시작 숫자
        t = Integer.parseInt(st.nextToken());  // 목표 숫자

        // 특수한 경우들 처리
        if (s == t) {
            // 시작 숫자와 목표 숫자가 같은 경우 - 연산 불필요
            result = "0";
            return;
        } else if (t == 0) {
            // 목표 숫자가 0인 경우 - s에서 s를 빼면 0이 됨
            result = "-";
            return;
        }

        visited = new HashSet<>();
        result = BFS();  // BFS로 최단 경로 탐색
    }

    // BFS를 통해 s에서 t까지의 최단 연산 순서를 찾는 메서드
    private static String BFS() {
        Queue<Element> q = new ArrayDeque<>();
        String result = "";

        visited.add(s);           // 시작 숫자를 방문 처리
        q.add(new Element(s));    // 시작 상태를 큐에 추가

        while (!q.isEmpty()) {
            Element current = q.poll();

            // 목표 숫자에 도달했다면 연산 순서 반환
            if (current.num == t) {
                result = current.opers;
                break;
            }

            // 3가지 연산(*, +, /)을 모두 시도
            for (int i = 0; i < 3; i++) {
                long next = current.num;
                char c = oper[i];

                // 각 연산에 따른 다음 숫자 계산
                if (oper[i] == '*') {
                    next *= current.num;    // 자기 자신을 곱하기 (n * n)
                } else if (oper[i] == '+') {
                    next += current.num;    // 자기 자신을 더하기 (n + n)
                } else {
                    next /= current.num;    // 자기 자신을 나누기 (n / n = 1)
                }

                // 다음 상태가 유효하지 않은 경우들을 건너뛰기
                if (next > 1e9 ||           // 10억을 초과하는 경우 (메모리 및 시간 절약)
                        visited.contains(next) || // 이미 방문한 숫자인 경우
                        next > t) {             // 목표 숫자보다 큰 경우 (더 이상 줄어들 수 없음)
                    continue;
                }

                visited.add(next);  // 새로운 숫자를 방문 처리
                // 새로운 상태를 큐에 추가 (숫자와 지금까지의 연산 순서)
                q.add(new Element(next, current.opers + c));
            }
        }

        // 목표 숫자에 도달할 수 없는 경우 -1 반환
        return result.isEmpty() ? "-1" : result;
    }

    // BFS에서 사용할 상태를 나타내는 클래스
    static class Element {
        long num;        // 현재 숫자
        String opers;    // 현재까지의 연산 순서

        // 시작 상태용 생성자 (연산 순서가 없음)
        Element(long num) {
            this.num = num;
            this.opers = "";
        }

        // 일반 상태용 생성자 (숫자와 연산 순서를 모두 받음)
        Element(long num, String s) {
            this.num = num;
            this.opers = s;
        }
    }
}