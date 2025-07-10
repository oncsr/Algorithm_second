import java.io.*;
import java.util.*;

public class Main {
    // 입출력을 위한 BufferedReader, BufferedWriter
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 전역 변수들
    private static Map<Character, Integer> map;  // 연산자 우선순위를 저장하는 맵
    private static Stack<Character> stack;       // 연산자를 임시 저장하는 스택
    private static String answer = "";           // 최종 후위 표기식 결과

    public static void main(String[] args) throws IOException {
        init();  // 입력 받기 및 중위→후위 변환 처리

        // 결과 출력
        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    // 입력 받기 및 중위 표기식을 후위 표기식으로 변환
    private static void init() throws IOException {
        stack = new Stack<>();
        map = new HashMap<>();

        // 연산자 우선순위 설정 (숫자가 작을수록 높은 우선순위)
        map.put('*', 1);  // 곱셈: 높은 우선순위
        map.put('/', 1);  // 나눗셈: 높은 우선순위
        map.put('+', 2);  // 덧셈: 낮은 우선순위
        map.put('-', 2);  // 뺄셈: 낮은 우선순위

        char[] input = br.readLine().toCharArray();

        // 중위 표기식의 각 문자를 순서대로 처리
        for (char c : input) {
            if (Character.isAlphabetic(c)) {
                // 피연산자(알파벳)인 경우: 바로 결과에 추가
                answer += c;

            } else if (c == '(') {
                // 여는 괄호인 경우: 스택에 푸시 (가장 높은 우선순위 처리)
                stack.push(c);

            } else if (c == ')') {
                // 닫는 괄호인 경우: 여는 괄호까지 모든 연산자를 팝하여 결과에 추가
                while (stack.peek() != '(') {
                    answer += stack.pop();
                }
                stack.pop();  // 여는 괄호 제거

            } else if (!stack.isEmpty() && stack.peek() != '(' && map.get(stack.peek()) <= map.get(c)) {
                // 현재 연산자보다 우선순위가 높거나 같은 연산자들을 모두 처리
                // 우선순위: 숫자가 작을수록 높은 우선순위이므로 <= 사용

                while (!stack.isEmpty() &&
                        stack.peek() != '(' &&
                        map.get(stack.peek()) <= map.get(c)) {
                    answer += stack.pop();
                }
                stack.push(c);  // 현재 연산자를 스택에 푸시

            } else {
                // 그 외의 경우: 현재 연산자를 스택에 푸시
                // - 스택이 비어있는 경우
                // - 스택 top이 여는 괄호인 경우
                // - 현재 연산자가 더 높은 우선순위인 경우
                stack.push(c);
            }
        }

        // 입력 처리가 끝난 후 스택에 남은 모든 연산자를 결과에 추가
        while (!stack.isEmpty()) {
            answer += stack.pop();
        }
    }
}