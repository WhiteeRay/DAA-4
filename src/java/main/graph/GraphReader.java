package graph;

import graph.models.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class GraphReader {
    private static final Gson gson = new Gson();

    public static Graph readGraphFromJson(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        reader.close();

        int n = jsonObject.get("n").getAsInt();
        Graph graph = new Graph(n);

        JsonArray edges = jsonObject.getAsJsonArray("edges");
        for (JsonElement edgeElement : edges) {
            JsonObject edge = edgeElement.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.get("w").getAsInt();
            graph.addEdge(u, v, w);
        }

        return graph;
    }

    public static int getSourceFromJson(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        reader.close();

        return jsonObject.get("source").getAsInt();
    }
}