//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    int[][] gragh;

    private int dfs(int[] initial, int original) {
        int n = gragh.length;
        boolean[] visited = new boolean[n];
        int[] count = {0};
        for (int i : initial) {
            if (original != i && !visited[i]) {
                dfs(n, original, i, visited, count);
            }
        }
        return count[0];
    }

    private void dfs(int n, int original, int root, boolean[] visited, int[] count) {
        count[0]++;
        visited[root] = true;
        for (int i = 0; i < n; i++) {
            if (!visited[i] && gragh[root][i] == 1) {
                dfs(n, original, i, visited, count);
            }
        }
    }

    public int minMalwareSpread(int[][] graph, int[] initial) {
        this.gragh = graph;
        int count = Integer.MAX_VALUE;
        int minM = -1;
        for (int i = 0; i < initial.length; i++) {
            int[] tmp = new int[initial.length - 1];
            int k = 0;
            for (int j = 0; j < initial.length; j++) {
                if (j != i) {
                    tmp[k++] = initial[j];
                }
            }
            int c = dfs(tmp, initial[i]);
            if (c < count || (c == count && initial[i] < minM)) {
                minM = initial[i];
                count = c;
            }
        }
        return minM;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
