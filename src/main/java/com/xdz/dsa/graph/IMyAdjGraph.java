package com.xdz.dsa.graph;

import com.xdz.dsa.list.IMyList;
import com.xdz.dsa.list.MySingleLinkedList;
import com.xdz.dsa.tree.bst.MyAvlTree;
import lombok.Data;

import java.util.Objects;

/**
 * Description: adjacent graph implement<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/28 13:51<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * let each vertex has one uniq no in one graph.
 * </pre>
 */
public interface IMyAdjGraph extends IMyGraph {
    /**
     * create a graph from edge-set.
     */
    void topSort();

    void shortedPath();

    void dfs();

    void bfs();

    // minimum spanning tree

    @Data
    class Vertex implements Comparable<Vertex> {
        String id;
        IMyList<Vertex> adjVertexes;

        int weight;
        int inDegree;
        int outDegree;

        String info;

        // 簿记变量
        int topNo;
        boolean visited;

        public Vertex(String id) {
            this.id = id;
            this.weight = 1;
            this.adjVertexes = new MySingleLinkedList<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vertex)) return false;
            Vertex vertex = (Vertex) o;
            return Objects.equals(id, vertex.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public int compareTo(Vertex o) {
            return id.compareTo(o.id);
        }
    }

    @Data
    class Edge {
        Vertex from;
        Vertex to;

        public Edge(Vertex from, Vertex to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            Edge edge = (Edge) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
}
