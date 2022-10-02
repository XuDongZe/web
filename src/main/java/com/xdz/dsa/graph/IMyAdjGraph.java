package com.xdz.dsa.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
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

    void dfs();

    void bfs();

    void shortedPath(Vertex v);

    // minimum spanning tree

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArcNode implements Comparable<ArcNode> {
        Vertex v;
        int weight;

        @Override
        public int compareTo(ArcNode o) {
            return Integer.compare(weight, o.weight);
        }
    }

    @Data
    class Vertex implements Comparable<Vertex>, Iterable<Vertex> {
        String id;
        // 第一个邻接点
        ArcNode arc;

        int inDegree;
        int outDegree;

        // 簿记变量
        int topNo;
        boolean visited;

        public Vertex(String id) {
            if (id == null) {
                throw new RuntimeException("Vertex id must not null");
            }
            this.id = id;
        }

        public Vertex getArcVertex() {
            return arc == null ? null : arc.v;
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

        @Override
        public Iterator<Vertex> iterator() {
            return new ArcVertexIterator(getArcVertex());
        }

        private static class ArcVertexIterator implements Iterator<Vertex> {

            Vertex next;

            public ArcVertexIterator(Vertex next) {
                this.next = next;
            }

            @Override
            public boolean hasNext() {
                return next != null && next.arc != null;
            }

            @Override
            public Vertex next() {
                Vertex next = this.next.arc.v;
                this.next = next;
                return next;
            }
        }
    }

    @Data
    class Edge {
        Vertex from;
        Vertex to;
        int weight;

        public Edge(Vertex from, Vertex to) {
            this.from = from;
            this.to = to;
            this.weight = 1;
        }

        public Edge(Vertex from, Vertex to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
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
