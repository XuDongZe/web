package com.xdz.dsa.graph;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * an weighted edge. un-directed.
 * v,w,weight: the edge's(v-w) is weight
 */
public class Edge implements Comparable<Edge> {
    private final int v; // one of vertex
    private final int w; // another vertex
    private final double weight; // weight of edge

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) {
            return w;
        } else if (vertex == w) {
            return v;
        } else {
            throw new RuntimeException("edge has no vertex: " + vertex);
        }
    }

    @Override
    public int compareTo(@NotNull Edge o) {
        return Double.compare(weight, o.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return v == edge.v && w == edge.w && Double.compare(edge.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v, w, weight);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.2f", v, w, weight);
    }
}
