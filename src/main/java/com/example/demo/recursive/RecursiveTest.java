package com.example.demo.recursive;

import java.util.ArrayList;
import java.util.List;

public class RecursiveTest {

}

class Node {
    public Double value;
    public List<Node> children;

    public Node(Double value) {
        this.value = value;
        this.children = new ArrayList<Node>();
    }

    public Node(Double value, List<Node> children) {
        this.value = value;
        this.children = children;
    }

    public Node() {
    }
}