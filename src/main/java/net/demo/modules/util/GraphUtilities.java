package net.demo.modules.util;

import org.springframework.stereotype.Component;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;

/**
 * Visualization graph data utilities
 * Based on cytoscape.js node, edge format
 * not yet Style, Layout 처리
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@Component
@SuppressWarnings("unchecked")
public class GraphUtilities {

	/**
	 * Node object list
	 */
	JSONArray nodes = new JSONArray();
	
	/**
	 * Edge object list
	 */
	JSONArray edges = new JSONArray();
	
	/**
	 * Creating NODE Object method
	 * 
	 * @param	id		graph node id
	 * @param	name	graph node name
	 * @return	graph node object return
	 */
	public void node(Object id, Object name) {
		JSONObject node = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("id",    id);
		data.put("name",  name);
		
		node.put("group", "nodes");
		node.put("data",  data);
		
		nodes.add(node);
	}
	
	/**
	 * Creating EDGE Object method
	 * 
	 * @param	id		graph edge id
	 * @param	name	graph edge label
	 * @param	source	graph edge start node id
	 * @param	target	graph edge end node id
	 * @return	graph edge object return
	 */
	public void edge(Object id, Object label, Object source, Object target) {
		JSONObject edge = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("id",     id);
		data.put("label",  label);
		data.put("source", source);
		data.put("target", target);
		
		edge.put("group",  "edges");
		edge.put("data",   data);
		
		edges.add(edge);
	}
	
	/**
	 * Bean Object 사용 시 / 쓰고 싶을 때
	 * Nodes, Edges를 초기화 하여 다른 Graph를 그릴 때 쓸 수 있도록 
	 * 
	 */
	public void graphInit() {
		nodes.clear();
		edges.clear();
	}
	
	/**
	 * toString overriding 
	 */
	@Override
	public String toString() {
		String text = "";
		
		text = "GraphFormat [";
		if(nodes != null) text += ", nodes="+nodes;
        if(edges != null) text += ", edges="+edges;      
		text += "]";
		
		return text;
	}
	
	/**
	 * Cytoscape.js Graph add format-data creating method
	 * 
	 * @return	Graph(Nodes, Edges) data return
	 */
	public JSONArray toJsonArray() {
		JSONArray result = new JSONArray();
		
		if(nodes != null) {
			for(int i=0; i<nodes.size(); i++) {
				result.add((JSONObject)nodes.get(i));
			}
		}
		
		if(edges != null) {
			for(int i=0; i<edges.size(); i++) {
				result.add((JSONObject)edges.get(i));
			}
		}

		return result;
	}
}
