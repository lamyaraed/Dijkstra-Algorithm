import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;

public class DijkstraGUI {
    private static JFrame jFrame;
    private JButton buttonNext;
    private JButton buttonStart;
    private JTextField nVertices;
    private JTextField nEdges;
    private JComboBox graphType;
    private JTextField SourceV;
    private JTextField DestV;
    private JButton buttonSetTable;
    private JTable tableSetGraph;
    private JPanel GUIPanel;
    private JScrollPane tableScrollPane;
    private JTable table1;

    private int nVert;
    private int nEdg;
    private int dircType;
    private int sVertex;
    private int eVertex;

    private Graph graph;
    private GraphGUI graphGUI;
    private Edge TempEdge;
    private Utils utils;

    public static void main(String[] args) {
        jFrame = new JFrame("Dijkstra Graph Algorithm");
        jFrame.setContentPane(new DijkstraGUI().GUIPanel);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    DijkstraGUI(){
        String[] column ={"Edges","From","To","Weight"};
        DefaultTableModel dtm = new DefaultTableModel(column,0);
        table1.setModel(dtm);
        buttonStart.setEnabled(false);

        buttonSetTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Vertices = nVertices.getText();
                String Edges = nEdges.getText();
               dircType = directionType((String) Objects.requireNonNull(graphType.getSelectedItem()));
                if (Vertices.equals("") || Edges.equals("")){
                    // Code To popup an ERROR_MESSAGE Dialog.
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid values for vertices, edges, and graph type!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    nVert = Integer.parseInt(Vertices);
                    nEdg = Integer.parseInt(Edges);

                    DefaultTableModel dtm = new DefaultTableModel(column, nEdg);
                    for(int rows=0; rows<nEdg; rows++){
                        dtm.setValueAt(rows+1, rows, 0);
                    }
                    table1.setModel(dtm);
                    buttonStart.setEnabled(true);
                }
            }
        });

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startVert = SourceV.getText();
                String endVert = DestV.getText();
                if (startVert.equals("") || endVert.equals("") || Integer.parseInt(startVert)>= nVert || Integer.parseInt(endVert) >= nVert){
                    // Code To popup an ERROR_MESSAGE Dialog.
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid values for start and end vertices",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    sVertex = Integer.parseInt(startVert);
                    eVertex = Integer.parseInt(endVert);
                    getTableData();
                }
            }
        });

    }

    void getTableData() {
        graph = new Graph(nVert);
        utils = new Utils();

        boolean dataProv = false;
        for (int ed = 0; ed <nEdg; ed++) {
            for (int val = 1; val < 4; val++) {
                if (table1.getModel().getValueAt(ed, val) == null) {
                    JOptionPane.showMessageDialog(jFrame, "There is one or more cells in table empty.",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    dataProv = false;
                    break;
                } else
                    dataProv = true;
            }
            if(!dataProv) break;
        }

        boolean valid = true;
        if (dataProv) {
            for (int i = 0; i <nEdg; i++) {
                int sVal, dVal, wVal;
                int col = 1;
                int row = i;
                String value = table1.getModel().getValueAt(row, col).toString();
                if (value.equals("") || Integer.parseInt(value) < 0 || Integer.parseInt(value) >= graph.Vertex) {
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid value for start vertex(from 0 to " + (nVert-1) + ")",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                } else
                    sVal = Integer.parseInt(value);

                col = 2;
                row = i;
                String Destvalue = table1.getModel().getValueAt(row, col).toString();
                if (Destvalue.equals("") || Integer.parseInt(Destvalue) < 0 || Integer.parseInt(Destvalue) >= graph.Vertex) {
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid value for end vertex (from 0 to " + (nVert-1) + ")",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                } else
                    dVal = Integer.parseInt(Destvalue);

                col = 3;
                row = i;
                String Weightvalue = table1.getModel().getValueAt(row, col).toString();
                if (Weightvalue.equals("") || Integer.parseInt(Weightvalue) < 0) {
                    JOptionPane.showMessageDialog(jFrame, "vertex weight should be 0 or greater",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                } else
                    wVal = Integer.parseInt(Weightvalue);


                TempEdge = new Edge(wVal, sVal, dVal);
                graph.edges.add(TempEdge);

            }
            if(valid) {
                System.out.println(dircType);
                graphGUI = new GraphGUI(nVert,nEdg,graph.edges, dircType);
                Vector<Integer> Answer = new Vector<>();
                Answer = utils.dijkstra(graph, dircType, sVertex, eVertex);
                graphGUI.getAnswer(Answer);
            }
        }
    }

    int directionType(String type){
        if (type.equals("undirected"))
            return 0;
        else
            return 1;
    }
}
