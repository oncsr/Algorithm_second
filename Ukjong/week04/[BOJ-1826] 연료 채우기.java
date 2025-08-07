import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static Car car;
    private static PriorityQueue<int[]> oils;
    private static int L, N, answer;

    public static void main(String[] args) throws IOException {
        init();
        answer = greedy();

        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        N = Integer.parseInt(br.readLine());
        oils = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[0], o2[0]));

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            oils.add(new int[]{a, b});
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        car = new Car(0, p);
    }

    private static int greedy() {
        int count = 0;

        for (int i = 1; i <= L; i++) {
            car.oil--;
            if (!oils.isEmpty() && oils.peek()[0] == i) {
                int[] temp = oils.poll();
                car.store.add(temp[1]);
            }

            if (car.oil == 0 && i < L && car.store.isEmpty()) return -1;
            if (i == L) break;
            if (car.oil == 0 && !car.store.isEmpty()) {
                car.oil += car.store.poll();
                count++;
            }
        }

        return count;
    }

    static class Car {
        int pos;
        int oil;
        PriorityQueue<Integer> store;

        public Car(int pos, int oil) {
            this.pos = pos;
            this.oil = oil;
            this.store = new PriorityQueue<>((o1, o2) -> Integer.compare(o2, o1));
        }
    }
}