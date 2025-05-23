package projatlab.algorithms.tools;

/** A Union-Find / Disjoint Set data structure 
 *  Tracks connected components (cells) 
 */

public class Unionfind {
    private final int[] parent;

    /** Constructs a new Union-Find data structure with a a specified size 
     * At the start, each cell is its own parent (own set)
     * @param size the number of elements
     */

    public Unionfind(int size){
        parent = new int[size];
        for (int i=0; i<size; i++){
            parent[i] = i;
        }
    }

    /** Finds the root of the set containing the element p 
     * @param p the element to find
     * @return the root of the set containing p 
     */

    public int find(int p){
        if (parent[p] != p){
            parent[p] = find(parent[p]);
        }
        return parent[p];
    }
    
    /** Merges the sets containinng elements p1 and p2 if they are not in the same set
     * If they are in the same set, nothing changes
     * @param p1 the first element
     * @param p2 the second element
     */
    
    public void union(int p1, int p2){

        int root1 = find(p1);
        int root2 = find(p2);
        if (root1 != root2){
            parent[root1] = root2;
        }

    }

    /** Checks if the two elements are in the same set 
     * @param p1 the first element
     * @param p2 the second element
     */

    public boolean connected(int p1, int p2){
        return find(p1) == find(p2);
    }

}
