import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static Map<Character, Integer> medicine;
    private static Set<String> visited;
    private static char[] input;
    private static int n, count;
    public static void main(String[] args) throws IOException {
        init();
        count = BFS();

        bw.write(count + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        n = Integer.parseInt(br.readLine());
        input = br.readLine().toCharArray();

        medicine = new HashMap<>();
        visited = new HashSet<>();
        medicine.put('B', 0);
        medicine.put('L', 1);
        medicine.put('D', 2);
    }

    private static int BFS() {
        Queue<int[]> q = new ArrayDeque<>();
        int result = 0;
        //다음에 먹어야 할 것, 먹은 약 개수, 남은 약의 왼쪽, 오른쪽
        visited.add(0 + "," + 0 + ","+ (3*n - 1));
        q.add(new int[]{0, 0, 0, 3*n - 1});

        while (!q.isEmpty()) {
            int[] current = q.poll();
            result = Math.max(result, current[1]);
            if (current[2] > current[3]) continue;

            for (int i = 2; i < 4; i++) {
                if (current[0] == medicine.get(input[current[i]])) {
                    if (i == 2 && !visited.contains((current[1] + 1) + "," + (current[2] + 1) + "," + current[3])) {
                        visited.add((current[1] + 1) + "," + (current[2] + 1) + "," + current[3]);
                        q.add(new int[]{(current[0] + 1) % 3, current[1] + 1, current[2] + 1, current[3]});
                    }
                    else if (i == 3 && !visited.contains((current[1] + 1) + "," + current[2] + "," + (current[3] - 1))) {
                        visited.add((current[1] + 1) + "," + current[2] + "," + (current[3] - 1));
                        q.add(new int[]{(current[0] + 1) % 3, current[1] + 1, current[2], current[3] - 1});
                    }
                }
            }
        }

        return result;
    }
}