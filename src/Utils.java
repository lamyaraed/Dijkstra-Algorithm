import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;


public class Utils {
    int start;
    int end;
    Vector<print>FinalAns = new Vector<>();
    Queue<Graph> EvalGraphs = new LinkedList<>();

    void initializePrint(Graph g,print[] arr){
        for (int i = 0; i <g.Vertex ; i++) {
            arr[i]=new print();
            arr[i].vertex = i;
            arr[i].distFromSource = Integer.MAX_VALUE;
            arr[i].PreviousVertex = Integer.MAX_VALUE;
        }
    }

    int minDistance(Graph graph,int dist[], Boolean sptSet[])
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int i = 0; i < graph.Vertex; i++)
            if (sptSet[i] == false && dist[i] <= min) {
                min = dist[i];
                min_index = i;
            }
        return min_index;
    }

    Vector<Integer> dijkstra(Graph g, int type, int startVer, int endVert)
    {
        // add initial graph
        EvalGraphs.add(g);

        this.start = startVer;
        this.end = endVert;
        print[] p = new print[g.Vertex];
        initializePrint(g,p);
        int temp = 0;
        int dist[] = new int[g.Vertex]; // The output array. dist[i] will hold
        // the shortest distance from src to i

        // sptSet[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean sptSet[] = new Boolean[g.Vertex];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < g.Vertex; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        // Distance of source vertex from itself is always 0
        dist[start] = 0;

        // Find shortest path for all vertices
        for (int count = 0; count < g.Vertex - 1; count++) {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(g,dist, sptSet);
            // Mark the picked vertex as processed
            sptSet[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < g.Vertex; v++) {
                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (type==0){

                    temp = getWeight(u, v, g);
                }
                if (type==1){
                    temp = getWeight2(u, v, g);
                }
                if (!sptSet[v] && temp != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + temp < dist[v]) {
                    dist[v] = dist[u] + temp;
                    p[v].PreviousVertex=u;
                    p[v].distFromSource=dist[v];
                }
            }
        }

        // print the constructed distance array
        printSolution(dist,g);
        return GetEndPath(p);
    }

    int getWeight(int first,int second,Graph g){
        for (int i = 0; i <g.edges.size() ; i++) {
            if ((g.edges.elementAt(i).source==first && g.edges.elementAt(i).dest==second) || g.edges.elementAt(i).source==second && g.edges.elementAt(i).dest==first)
                return g.edges.elementAt(i).weight;
        }
        return 0;
    }

    int getWeight2(int first,int second,Graph g){
        for (int i = 0; i <g.edges.size() ; i++) {
            if ((g.edges.elementAt(i).source==first && g.edges.elementAt(i).dest==second))
                return g.edges.elementAt(i).weight;
        }
        return 0;
    }

    void printSolution(int dist[], Graph g)
    {
        System.out.println("Vertex \t\t Distance from Source");
        for (int i = 0; i < g.Vertex; i++)
            System.out.println(i + " \t\t " + dist[i]);
    }

    /*void Print(Graph g, print []p){
        for (int i = 0; i <g.Vertex ; i++) {
            System.out.println("Vertex "+ p[i].vertex+" previous vertex "+p[i].PreviousVertex+" dist from source = "+p[i].distFromSource);
        }
    }*/

    Vector<Integer> GetEndPath(print[]p){
        Vector<Integer>ans = new Vector<>();
        int prevNode = p[end].PreviousVertex;
        ans.add(end);
        ans.add(prevNode);
        while(prevNode != start){
            prevNode = p[prevNode].PreviousVertex ;
            ans.add(prevNode);
        }
        for (int i = ans.size()-1; i >=0 ; i--) {
            System.out.print(ans.elementAt(i)+" ");
        }
        return ans;
    }
}
