package net.demo.modules.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;

/**
 * Visualization graph data utilities
 * Based on cytoscape.js node, edge format
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@Component
@SuppressWarnings("unchecked")
public class GraphUtilities {

	@SuppressWarnings("serial")
	public Map<String, Object> color_list = new HashMap<String, Object>(){

		/**
		 * color list
		 */
		{
			// 1. movie
			put("movie",  "#D0A9F5");
			// 2. person
			put("person", "#FE2E2E");
		}
	};
	
	@SuppressWarnings("serial")
	public Map<String, Object> style_list = new HashMap<String, Object>(){

		/**
		 * style_list
		 */
		{
			// 1. cluster style
			put("cluster", new JSONObject(new HashMap<String,Object>(){
				{
					put("height", "200px");
					put("width",  "200px");
				}
			}));
			
			// 2. bold
			put("bold", new JSONObject(new HashMap<String,Object>(){
				{
					put("text-outline-color", "black");
					put("border-color",       "black");
				}
			}));
			
			// 3. etc 등등등
			put("etc", new JSONObject(new HashMap<String,Object>(){
				{
					put("height", "200px");
					put("width",  "200px");
					put("color",  "black");
				}
			}));
			
			// 4. edge
			put("edge", new JSONObject(new HashMap<String,Object>(){
				{
					put("background-color",   "#1abde8");
				}
			}));
			
			// 5. node
			put("node", new JSONObject(new HashMap<String,Object>(){
				{
					put("text-outline-color", "black");
					put("text-outline-width", 2);
				}
			}));
		}
	};
	
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
	 * Creating NODE Object with style method 
	 * 
	 * @param	id		graph node id
	 * @param	name	graph node name
	 * @param	label	graph node label
	 * @param	style	graph node style object
	 * @return	graph node object return
	 */
	public void node(Object id, Object name, Object label, JSONObject style) {
		JSONObject node = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("id",    id);
		data.put("name",  name);
		data.put("label", label);
		
		node.put("group", "nodes");
		node.put("data",  data);
		node.put("style", style);
		
		nodes.add(node);
	}
	
	/**
	 * Creating NODE Object with style method 
	 * 
	 * @param	id		graph node id
	 * @param	name	graph node name
	 * @param	label	graph node label
	 * @param	style	graph node style string name
	 * @return	graph node object return
	 */
	public void node(Object id, Object name, Object label, String style) {
		JSONObject node = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("id",    id);
		data.put("name",  name);
		data.put("label", label);
		
		node.put("group", "nodes");
		node.put("data",  data);
		
		if("movie".equals(style.toLowerCase())) { node.put("style", style_list.get("cluster")); }
		else if("node".equals(style.toLowerCase())) {
			Map<String, Object> styleObj = (Map<String,Object>)style_list.get("node");
			styleObj.put("background-color", color_list.get(label));
		
			node.put("style", new JSONObject(styleObj));
		}
		
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
	 * Creating EDGE Object method
	 * 
	 * @param	id		graph edge id
	 * @param	label	graph edge label
	 * @param	source	graph edge start node id
	 * @param	target	graph edge end node id
	 * @param	style   graph edge style object
	 * @return	graph edge object return
	 */
	public void edge(Object id, Object label, Object source, Object target, JSONObject style) {
		JSONObject edge = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("id",     id);
		data.put("label",  label);
		data.put("source", source);
		data.put("target", target);
		
		edge.put("group",  "edges");
		edge.put("data",   data);
		edge.put("style",  style);
		
		edges.add(edge);
	}
	
	/**
	 * Creating EDGE Object method
	 * 
	 * @param	id		graph edge id
	 * @param	label	graph edge label
	 * @param	source	graph edge start node id
	 * @param	target	graph edge end node id
	 * @param	style   graph edge style name
	 * @return	graph edge object return
	 */
	public void edge(Object id, Object label, Object source, Object target, String style) {
		JSONObject edge = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("id",     id);
		data.put("label",  label);
		data.put("source", source);
		data.put("target", target);
		
		edge.put("group",  "edges");
		edge.put("data",   data);
		if("edge".equals(style.toLowerCase())) { edge.put("style", style_list.get("edge")); }
		
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

		graphInit();
		
		return result;
	}
}
