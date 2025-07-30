import java.io.*;
import java.util.*;

public class Main {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    private static Room[] maze;
    private static boolean[][] visited;
    private static int n;

    public static void main(String[] args) throws IOException {
        while (true) {
            n = Integer.parseInt(br.readLine());
            if (n == 0) break;
            init();
            if (BFS()) {
                bw.write("Yes" + "\n");
            } else {
                bw.write("No" + "\n");
            }
        }

        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() throws IOException {
        maze = new Room[n + 1];
        visited = new boolean[n + 1][500 + 1];

        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            char state = st.nextToken().charAt(0);
            int money = Integer.parseInt(st.nextToken());

            maze[i] = new Room(state, money);
            while (true) {
                int room = Integer.parseInt(st.nextToken());
                if (room == 0) break;
                maze[i].connect.add(room);
            }
        }
    }

    private static boolean BFS() {
        Queue<int[]> q = new ArrayDeque<>();
        visited[1][0] = true;
        q.add(new int[] {1, 0});

        while (!q.isEmpty()) {
            int[] current = q.poll();

            if (current[0] == n) return true;

            for (int room : maze[current[0]].connect) {
                int money = current[1];
                char state = maze[room].state;
                if (state == 'L' && money < maze[room].money) money = maze[room].money;
                if (state == 'T') {
                    if (current[1] >= maze[room].money)  money = current[1] - maze[room].money;
                    else continue;
                }

                if (visited[room][money]) continue;
                visited[room][money] = true;
                q.add(new int[] {room, money});
            }
        }

        return false;
    }

    static class Room {
        char state;
        int money;
        List<Integer> connect;

        public Room(char state, int money) {
            this.state = state;
            this.money = money;
            this.connect = new ArrayList<>();
        }
    }
}