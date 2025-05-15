package projatlab.algorithms.tools;

public class Unionfind {
    private final int[] parent;

    public Unionfind(int size){
        parent = new int[size];
        for (int i=0; i<size; i++){
            parent[i] = i;
        }
    }

    public int find(int p){
        if (parent[p] != p){
            parent[p] = find(parent[p]);
        }
        return parent[p];
    }
    
    public void union(int p1, int p2){

        int root1 = find(p1);
        int root2 = find(p2);
        if (root1 != root2){
            parent[root1] = root2;
        }

    }

    public boolean connected(int p1, int p2){
        return find(p1) == find(p2);
    }

}
