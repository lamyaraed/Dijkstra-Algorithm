import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.samples.SimpleGraphDraw;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationImageServer;

import javax.swing.*;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;


////todo https://stackoverflow.com/questions/35300510/vertex-label-color-in-jung-visualization  ------- MUST READ ---------

public class GraphGUI {
    static SparseGraph<Integer, Edge> directedGraph;
    private Layout<Integer, Edge> graphLayout;
    static BasicVisualizationServer<Integer,Edge> vs;
    Vector<Edge> VecEdges = new Vector<>();
    Vector<Integer> VisitedVect = new Vector<>();
    Vector<Integer> ResultGraph = new Vector<>();
    int Vertices, Edges, Direction, CurrVert=99999;
    static int Counter = 0;
    JFrame frame;
    Graph graph;
    JButton buttonNext;

    GraphGUI(int V, int E, Vector<Edge> ed, int Direction){
        this.Vertices = V;
        this.Edges = E;
        this.Direction = Direction;
        directedGraph = new SparseGraph<>();
        for(int i=0; i<Vertices; i++){
            directedGraph.addVertex(i);
        }
        for(Edge e: ed){
            Edge myEdge = new Edge(e.weight, e.source, e.dest);
            VecEdges.add(myEdge);
        }
        createGraph();
    }

    void drawFrame(){
        frame = new JFrame();
        frame.setName("Graph");
        frame.getContentPane().add(vs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(450,450));
        buttonNext = new JButton("Next");
        buttonNext.setEnabled(true);
        frame.add(buttonNext, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Counter == 0){
                    while(directedGraph.getEdgeCount() >0){
                        for(Edge myEdge : directedGraph.getEdges())
                            directedGraph.removeEdge(myEdge);
//                        System.out.println("still");
                    }
                    //SwingUtilities.updateComponentTreeUI(frame);
                    vs.repaint();
                    Counter++;
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                    //System.out.println("here");
                    //graphLayout.setGraph(directedGraph);
                    //vs = new BasicVisualizationServer<Integer, Edge>(graphLayout);
                }
                else if (ResultGraph.size() != 0){
                    Next();
                    vs.repaint();
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                }
                else{
                    JOptionPane.showMessageDialog(frame, "We reached the result graph!",
                            "CONGRATS", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    void createGraph(){
        graphLayout = new CircleLayout(directedGraph);
        graphLayout.setSize(new Dimension(450,450));
        vs = new BasicVisualizationServer<Integer, Edge>(graphLayout);
        vs.setPreferredSize(new Dimension(450,450));
        drawVertices();
        drawEdges();
        drawFrame();
    }

    void drawVertices(){
        for(int i=0; i<Vertices; i++){

            Transformer<Integer, Paint> vertexPaint = new Transformer<Integer,Paint>() {
                public Paint transform(Integer i) {
                    return Color.GRAY;
                }
            };
            vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
            vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
            vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        }
    }

    void drawEdges(){
        for(Edge e: VecEdges){
            if(Direction == 0)
                directedGraph.addEdge(new Edge(e.weight, e.source, e.dest), e.source, e.dest, EdgeType.UNDIRECTED);
            else
                directedGraph.addEdge(new Edge(e.weight,e.source,e.dest), e.source, e.dest, EdgeType.DIRECTED);
            vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Edge, String>(){
                public String transform(Edge link) {
                    return Integer.toString(link.weight);
                }
            });
        }
    }

    void Next() {
        Edge CurrentEdg = null;
        if(Direction == 1){
            for(Edge y: VecEdges){
                if(y.source == CurrVert && y.dest == ResultGraph.get(ResultGraph.size()-1)){
                    CurrentEdg = y;
                    ResulEdgeDraw(CurrentEdg);
                }
            }
        }
        else{
            for(Edge y: VecEdges){
                if((y.source == CurrVert && y.dest == ResultGraph.get(ResultGraph.size()-1)) ||
                        (y.dest == CurrVert && y.source == ResultGraph.get(ResultGraph.size()-1))){
                    CurrentEdg = y;
                    ResulEdgeDraw(CurrentEdg);
                }
            }
        }
        CurrVert = ResultGraph.get(ResultGraph.size()-1);
        //System.out.println(ResultGraph.get(ResultGraph.size()-1));
        ResultGraph.remove(ResultGraph.size()-1);
        VisitedVect.add(CurrVert);
        //System.out.println(CurrVert);
        changeVertColour(CurrVert);
    }

    void getAnswer(Vector<Integer> Answer){
        for(int i: Answer){
            ResultGraph.add(i);
        }
    }

    void ResulEdgeDraw(Edge edge){
        //System.out.println("here");
        if(Direction == 0)
            directedGraph.addEdge(new Edge(edge.weight, edge.source, edge.dest), edge.source, edge.dest, EdgeType.UNDIRECTED);
        else
            directedGraph.addEdge(new Edge(edge.weight,edge.source,edge.dest), edge.source, edge.dest, EdgeType.DIRECTED);
        vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Edge, String>(){
            public String transform(Edge link) {
                return Integer.toString(link.weight);
            }
        });

//        vs.getRenderContext().setEdgeFillPaintTransformer(new org.apache.commons.collections15.Transformer<Edge,Paint>(){
//            public Paint transform(Edge i) {
//                return Color.GREEN;
//            }
//        });
    }

    void changeVertColour(int Ver){
        //Transformer;
        Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                if(VisitedVect.contains(i))
                    return Color.GREEN;
                else
                    return Color.GRAY;
            }
        };
        vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
    }
}
