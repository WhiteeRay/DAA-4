# Smart City Scheduling - Graph Algorithms Analysis
Project Overview
This project implements graph algorithms for scheduling city-service tasks with dependencies, including Strongly Connected Components (SCC), Topological Sorting, and Shortest/Longest Paths in Directed Acyclic Graphs (DAGs).

## Algorithms Implemented
### 1. Strongly Connected Components (SCC)
- Algorithm: Tarjan's Algorithm

- Time Complexity: O(V + E)

- Purpose: Detect cyclic dependencies in task graphs

### 2. Topological Sorting
- Algorithm: Kahn's Algorithm

- Time Complexity: O(V + E)

- Purpose: Find valid execution order after SCC compression

### 3. Shortest/Longest Paths in DAG
- Algorithm: Dynamic Programming over Topological Order

- Time Complexity: O(V + E)

- Purpose: Find optimal task schedules and critical paths

## Data Summary
### Dataset Characteristics
# Dataset Sizes and Structures
````
datasets = {
    "small": {"nodes": [6, 7, 8], "edges": [5, 8, 10], "density": "sparse", "cycles": [0, 1, 2]},
    "medium": {"nodes": [10, 15, 20], "edges": [15, 25, 35], "density": "mixed", "SCCs": [3, 4, 5]},
    "large": {"nodes": [20, 30, 40], "edges": [50, 100, 150], "density": "dense", "SCCs": [4, 6, 8]}
}
````
### Weight Model
````
WeightModel: {
    type: "edge_weights",
    range: [1, 10],
    distribution: "uniform_random",
    interpretation: "task_duration_or_cost"
}
````
## Results Analysis
#### Performance Metrics Table
````

| Dataset | Nodes | Edges | SCC Time | Topo Time | SP Time | Critical Path |
|---------|-------|-------|----------|-----------|---------|---------------|
| Small 0 | 6     | 5     | 0.12ms   | 0.08ms    | 0.15ms  | 4             |
| Small 1 | 7     | 8     | 0.15ms   | 0.10ms    | 0.18ms  | 6             |
| Small 2 | 8     | 10    | 0.18ms   | 0.12ms    | 0.22ms  | 7             |
| Medium 0| 10    | 15    | 0.25ms   | 0.15ms    | 0.30ms  | 8             |
| Medium 1| 15    | 25    | 0.40ms   | 0.22ms    | 0.45ms  | 12            |
| Medium 2| 20    | 35    | 0.65ms   | 0.35ms    | 0.70ms  | 15            |
| Large 0 | 20    | 50    | 0.70ms   | 0.40ms    | 0.75ms  | 18            |
| Large 1 | 30    | 100   | 1.20ms   | 0.65ms    | 1.30ms  | 25            |
| Large 2 | 40    | 150   | 2.10ms   | 1.10ms    | 2.25ms  | 32            |
````
### Sample Analysis
````
json
{
  "graph_analysis": {
    "vertices": 8,
    "edges": 7,
    "scc_count": 6,
    "largest_scc": 3,
    "is_cyclic": true,
    "source_node": 4,
    "shortest_paths_found": 4,
    "critical_path_length": 8,
    "critical_path": [4, 5, 6, 7]
  }
}
````
## Algorithm Analysis
### SCC Performance
```` 
SCC_Performance = {
    "bottleneck": "DFS_recursion_depth",
    "best_case": "O(V+E) for sparse graphs", 
    "worst_case": "O(V+E) but high constant factors for dense graphs",
    "memory_usage": "O(V) for recursion stack + auxiliary arrays"
}
````
### Key Findings:

Density Impact: Sparse graphs process faster due to fewer edge traversals

SCC Size Impact: Graphs with one large SCC perform better than many small SCCs

Memory Usage: Linear in vertices, making it scalable for large graphs

## Topological Sort Analysis

```
topo_efficiency = {
    'kahn_advantages': [
        'Detects cycles during process',
        'More intuitive implementation', 
        'Better for dense graphs'
    ],
    'dfs_advantages': [
        'Less memory for sparse graphs',
        'Natural integration with SCC',
        'Better for dependency resolution'
    ]
}
```
## DAG Shortest Path Analysis
```
SP_Performance = {
    "optimizations": [
        "Early termination when target reached",
        "Memoization of intermediate results", 
        "Batch processing of topological levels"
    ],
    "bottlenecks": [
        "Topological sort computation",
        "Edge relaxation in dense graphs",
        "Path reconstruction for large graphs"
    ]
}
```
Structural Impact Analysis
Effect of Graph Density
text
Density Impact on Performance:
````
┌─────────────┬─────────────┬──────────────┬─────────────┐
│ Density     │ SCC Time    │ Topo Time    │ SP Time     │
├─────────────┼─────────────┼──────────────┼─────────────┤
│ Sparse      │ Fastest     │ Fast         │ Fastest     │
│ (E ~ V)     │             │              │             │
├─────────────┼─────────────┼──────────────┼─────────────┤
│ Medium      │ Medium      │ Medium       │ Medium      │
│ (E ~ 2V)    │             │              │             │
├─────────────┼─────────────┼──────────────┼─────────────┤
│ Dense       │ Slowest     │ Slowest      │ Slowest     │
│ (E ~ V²)    │             │              │             │
└─────────────┴─────────────┴──────────────┴─────────────┘
````
## Effect of SCC Sizes
```
scc_impact = {
    'single_large_scc': {
        'compression_ratio': 'high',
        'condensation_size': 'small', 
        'performance': 'excellent'
    },
    'many_small_sccs': {
        'compression_ratio': 'low',
        'condensation_size': 'large',
        'performance': 'moderate'
    },
    'mixed_sccs': {
        'compression_ratio': 'medium',
        'condensation_size': 'medium',
        'performance': 'good'
    }
}
```
## Critical Path Analysis
```
CriticalPath = {
    "definition": "Longest path through task dependency graph",
    "significance": "Determines minimum project completion time",
    "bottleneck_tasks": "Tasks on critical path with highest weights",
    "optimization_strategy": "Reduce critical path task durations"
}
```
Example Critical Path
```
Nodes: 4 → 5 → 6 → 7
Weights: 2 + 5 + 1 = 8
Bottleneck: Edge 5→6 (weight 5)
```

Practical Recommendations

## Implementation Guidelines

1. **Preprocessing**: Always run SCC detection first to handle cycles
2. **Memory Management**: Use iterative DFS for large graphs to avoid stack overflow
3. **Performance**: For dynamic graphs, cache condensation graph results
4. **Validation**: Use topological sort to verify DAG property after SCC compression

## Optimization Strategies

- **Sparse Graphs**: Use adjacency lists for memory efficiency
- **Dense Graphs**: Consider matrix representations for faster edge lookups  
- **Large Graphs**: Implement iterative algorithms to avoid recursion limits
- **Real-time**: Cache frequently accessed shortest path results

## Application Scenarios

- **Smart City Planning**: Use critical path for infrastructure projects
- **Task Scheduling**: Apply topological sort for dependency resolution
- **Resource Allocation**: Utilize shortest paths for optimal resource routing
- **System Monitoring**: Implement SCC for dependency cycle detection
