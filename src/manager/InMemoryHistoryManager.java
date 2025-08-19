package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodes;
    private Node lastNode;
    private Node firstNode;

    public InMemoryHistoryManager() {
        nodes = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            firstNode = node.next;
            if (firstNode != null) {
                firstNode.prev = null;
            }
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            lastNode = node.prev;
            if (lastNode != null) {
                lastNode.next = null;
            }
        }

        node.prev = null;
        node.next = null;

        nodes.remove(node.value.getId());
    }

    private void linkLast(Task task) {
        Node existingNode = nodes.get(task.getId());
        if (existingNode != null && existingNode == lastNode) {
            return;
        }
        if (existingNode != null) {
            remove(task.getId());
        }

        Node newNode = new Node(task, lastNode, null);
        if (lastNode != null) {
            lastNode.next = newNode;
        } else {
            firstNode = newNode;
        }
        lastNode = newNode;
        nodes.put(task.getId(), newNode);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = firstNode;

        while (current != null) {
            tasks.add(current.value);
            current = current.next;
        }

        return tasks;
    }

    private static class Node {
        private final Task value;
        private Node prev;
        private Node next;

        public Node(Task value, Node prev, Node next) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }
}
