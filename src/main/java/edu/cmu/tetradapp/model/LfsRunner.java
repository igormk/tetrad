///////////////////////////////////////////////////////////////////////////////
// For information as to what this class does, see the Javadoc, below.       //
// Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006,       //
// 2007, 2008, 2009, 2010, 2014, 2015 by Peter Spirtes, Richard Scheines, Joseph   //
// Ramsey, and Clark Glymour.                                                //
//                                                                           //
// This program is free software; you can redistribute it and/or modify      //
// it under the terms of the GNU General Public License as published by      //
// the Free Software Foundation; either version 2 of the License, or         //
// (at your option) any later version.                                       //
//                                                                           //
// This program is distributed in the hope that it will be useful,           //
// but WITHOUT ANY WARRANTY; without even the implied warranty of            //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             //
// GNU General Public License for more details.                              //
//                                                                           //
// You should have received a copy of the GNU General Public License         //
// along with this program; if not, write to the Free Software               //
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA //
///////////////////////////////////////////////////////////////////////////////

package edu.cmu.tetradapp.model;

import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.data.DataModelList;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.*;
import edu.cmu.tetrad.search.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends AbstractAlgorithmRunner to produce a wrapper for the GES algorithm.
 *
 * @author Ricardo Silva
 */

public class LfsRunner extends AbstractAlgorithmRunner implements
        GraphSource, PropertyChangeListener {
    static final long serialVersionUID = 23L;
    private transient List<PropertyChangeListener> listeners;
    private Graph pattern;

    // ============================CONSTRUCTORS============================//

    // public LingamPatternRunner(DataWrapper dataWrapper, PcSearchParams
    // params) {
    // super(dataWrapper, params);
    // }

    public LfsRunner(GraphWrapper graphWrapper,
                               DataWrapper dataWrapper, PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = graphWrapper.getGraph();
    }

    public LfsRunner(GraphWrapper graphWrapper,
                               DataWrapper dataWrapper, PcSearchParams params,
                               KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
        this.pattern = graphWrapper.getGraph();
    }

    /**
     * Constucts a wrapper for the given EdgeListGraph.
     */
    public LfsRunner(GraphSource graphWrapper, PcSearchParams params,
                               KnowledgeBoxModel knowledgeBoxModel) {
        super(graphWrapper.getGraph(), params, knowledgeBoxModel);
    }

    /**
     * Constucts a wrapper for the given EdgeListGraph.
     */
    public LfsRunner(GraphSource graphWrapper, PcSearchParams params) {
        super(graphWrapper.getGraph(), params, null);
    }

    public LfsRunner(PcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params, KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(PcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(CpcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params, KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(CpcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(JpcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params, KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(JpcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(JcpcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params, KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(JcpcRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(FciRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(CcdRunner wrapper, DataWrapper dataWrapper,
                               BasicSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(IGesRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params, KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
        this.pattern = wrapper.getGraph();
    }

    public LfsRunner(IGesRunner wrapper, DataWrapper dataWrapper,
                               PcSearchParams params) {
        super(dataWrapper, params, null);
        this.pattern = wrapper.getGraph();
    }

    /**
     * Generates a simple exemplar of this class to test serialization.
     *
     * @see edu.cmu.TestSerialization
     * @see edu.cmu.tetradapp.util.TetradSerializableUtils
     */
    public static LingamStructureRunner serializableInstance() {
        return new LingamStructureRunner(DataWrapper.serializableInstance(),
                PcSearchParams.serializableInstance(), KnowledgeBoxModel
                        .serializableInstance());
    }

    // ============================PUBLIC METHODS==========================//

    /**
     * Executes the algorithm, producing (at least) a result workbench. Must be implemented in the extending class.
     */

    public void execute() {
        DataModel source = getDataModel();
        Graph graph = null;

        if (source instanceof DataModelList) {
//            graph = lingamPatternEdgeVote((DataModelList) source, pattern);
            graph = multiLingamPattern((DataModelList) source, pattern);
        } else {
            DataModelList list = new DataModelList();
            list.add(source);

            graph = multiLingamPattern(list, pattern);
        }

        setResultGraph(graph);

        if (getSourceGraph() != null) {
            GraphUtils.arrangeBySourceGraph(graph, getSourceGraph());
        } else if (getParams().getKnowledge().isDefaultToKnowledgeLayout()) {
            SearchGraphUtils.arrangeByKnowledgeTiers(graph, getParams().getKnowledge());
        } else {
            GraphUtils.circleLayout(graph, 200, 200, 150);
        }


        // for (int i = 0; i < result.getDags().size(); i++) {
        // System.out.println("\n\nModel # " + (i + 1) + " # votes = " +
        // result.getCounts().get(i));
        // System.out.println(result.getDags().get(i));
        // }
    }

    private Graph lingamPatternEdgeVote(DataModelList dataSets, Graph pattern) {
        List<Graph> lingamPatternGraphs = new ArrayList<Graph>();

        // Images plus lingam orientation on multiple subjects.
        for (DataModel dataModel : dataSets) {
            DataSet dataSet = (DataSet) dataModel;
            LingamPattern lingamPattern = new LingamPattern(pattern, dataSet);
            lingamPattern.setAlpha(getParams().getIndTestParams().getAlpha());
            Graph _graph = lingamPattern.search();

            System.out.println(_graph);

            lingamPatternGraphs.add(_graph);
        }

        Graph lingamizedGraph = new EdgeListGraph(pattern.getNodes());

        for (Edge edge : pattern.getEdges()) {
            int numRight = 0, numLeft = 0;

            for (Graph graph : lingamPatternGraphs) {
                if (graph.containsEdge(Edges.directedEdge(edge.getNode1(), edge.getNode2()))) {
                    numRight++;
                } else if (graph.containsEdge(Edges.directedEdge(edge.getNode2(), edge.getNode1()))) {
                    numLeft++;
                }
            }

            int margin = 0;

            if (numRight > numLeft + margin) {
                lingamizedGraph.addDirectedEdge(edge.getNode1(), edge.getNode2());
            } else if (numLeft > numRight + margin) {
                lingamizedGraph.addDirectedEdge(edge.getNode2(), edge.getNode1());
            } else {
                lingamizedGraph.addUndirectedEdge(edge.getNode1(), edge.getNode2());
            }
        }

        System.out.println("lingamized graph = " + lingamizedGraph);

        return lingamizedGraph;
    }

    private Graph multiLingamPattern(DataModelList dataSets, Graph pattern) {
        final PcSearchParams params = (PcSearchParams) getParams();
        List<DataSet> _dataSets = new ArrayList<DataSet>();

        for (DataModel dataModel : dataSets) {
            _dataSets.add((DataSet) dataModel);
        }

        Lofs lofs = new Lofs(pattern, _dataSets);
        lofs.setAlpha(getParams().getIndTestParams().getAlpha());
        lofs.setR1Done(params.isR1Done());
        lofs.setR2Done(params.isR2Done());
        lofs.setStrongR2(!params.isOrientStrongerDirection());
        lofs.setMeekDone(params.isMeekDone());

        Graph graph = lofs.orient();

        return graph;
    }

    public Graph getGraph() {
        return getResultGraph();
    }

    /**
     * @return the names of the triple classifications. Coordinates with getTriplesList.
     */
    public List<String> getTriplesClassificationTypes() {
        List<String> names = new ArrayList<String>();
        names.add("Colliders");
        names.add("Noncolliders");
        return names;
    }

    /**
     * @return the list of triples corresponding to <code>getTripleClassificationNames</code> for the given node.
     */
    public List<List<Triple>> getTriplesLists(Node node) {
        List<List<Triple>> triplesList = new ArrayList<List<Triple>>();
        Graph graph = getGraph();
        triplesList.add(GraphUtils.getCollidersFromGraph(node, graph));
        triplesList.add(GraphUtils.getNoncollidersFromGraph(node, graph));
        return triplesList;
    }

    public boolean supportsKnowledge() {
        return true;
    }

    public ImpliedOrientation getMeekRules() {
        MeekRules rules = new MeekRules();
        rules.setKnowledge(getParams().getKnowledge());
        return rules;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(evt);
    }

    private void firePropertyChange(PropertyChangeEvent evt) {
        for (PropertyChangeListener l : getListeners()) {
            l.propertyChange(evt);
        }
    }

    private List<PropertyChangeListener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<PropertyChangeListener>();
        }
        return listeners;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (!getListeners().contains(l))
            getListeners().add(l);
    }

    public IndependenceTest getIndependenceTest() {
        Object dataModel = getDataModel();

        if (dataModel == null) {
            dataModel = getSourceGraph();
        }

        IndTestType testType = (getParams()).getIndTestType();
        return new IndTestChooser().getTest(dataModel, getParams(), testType);
    }
}


