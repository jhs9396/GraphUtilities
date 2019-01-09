package net.demo.modules.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;
import net.bitnine.agensgraph.deps.org.json.simple.parser.JSONParser;
import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.graph.property.JsonObject;
import net.demo.modules.logger.UDLogger;

/**
 * Query ResultSet formatting utilities
 * 
 * @author HyeonSu Jeon
 * @version 0.2
 * @since 0.1
 */
@Component
@SuppressWarnings("unchecked")
public class FormatUtilities {
	@UDLogger Logger logger;
	/**
	 * Convert a ResultSet into a JSONArray
	 *
	 * @param ResultSet
	 * @throws SQLException,JSONException
	 * @return JSONArray
	 */
	public JSONArray convertToJSONArray(ResultSet resultSet) throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < total_rows; i++) {
				obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
			}
			jsonArray.add(obj);
		}
		return jsonArray;
	}

	/**
	 * Convert a ResultSet that it is similar to Neo4j format 
	 * {graph:{nodes:[...],edges:[...],etc:[]}}
	 *
	 * @param resultSet   cypher query result set list
	 * @throws Exception  When logic that is parser, converting was executed by error
	 * @return After resultSet parsing, JSONObject return 
	 */
	@SuppressWarnings("resource")
	public JSONObject convertToGraphObj(ResultSet resultSet) throws Exception {
		JSONObject resJson    = new JSONObject();   // formatting response result object 
		JSONObject graph      = new JSONObject();   // graph format similar to Neo4j 
		JSONArray  nodes      = new JSONArray();    // node list object array
		JSONArray  edges      = new JSONArray();    // edge list object array
		JSONArray  etc        = new JSONArray();    // etc list object array
		JSONParser parser     = new JSONParser();   // net.bitnine.JsonObject -> org.simple.JSONObject parser
		
		/** phase 1 : ResultSet list while loop **/
		while (resultSet.next()) {
			
			int total_rows = resultSet.getMetaData().getColumnCount();
			
			for (int i = 0; i < total_rows; i++) {
				
				// Column information is 'VERTEX'
				if("vertex".equals(resultSet.getMetaData().getColumnTypeName(i+1).toLowerCase())) {
					
					Vertex     vertex  = (Vertex)resultSet.getObject(i+1);
					JsonObject obj     = new JsonObject();
					
					obj.put("id",         vertex.getVertexId().toString());
					obj.put("properties", vertex.getProperty());
					obj.put("label",      vertex.getLabel());
					
					String objStr = obj.toString();
					
					nodes.add(parser.parse(objStr));
					
				}
				// Column information is 'EDGE'
				else if("edge".equals(resultSet.getMetaData().getColumnTypeName(i+1).toLowerCase())) {
					
					Edge       edge = (Edge)resultSet.getObject(i+1);
					JsonObject obj  = new JsonObject();
					
					obj.put("id",         edge.getEdgeId().toString());
					obj.put("properties", edge.getProperty());
					obj.put("label",      edge.getLabel());
					obj.put("startNode",  edge.getStartVertexId().toString());
					obj.put("endNode",    edge.getEndVertexid().toString());

					String objStr = obj.toString();
					
					edges.add(parser.parse(objStr));
					
				}
				// Column information isn't 'VERTEX' or 'EDGE'
				else { 
					
					etc.add(resultSet.getObject(i+1));
					
				}
			}
		}
		
		/** phase 2. objects contain graph JSONObject **/
		if(nodes.size() > 0) { graph.put("nodes", nodes); }
		if(edges.size() > 0) { graph.put("edges", edges); }
		if(etc.size()   > 0) { graph.put("etc",   etc);   }
		
		/** phase 3. graph object return **/
		resJson.put("graph", graph);
		
		return resJson;
	}
	
	/**
	 * Convert a ResultSet into a String XMLArray
	 *
	 * @param  ResultSet
	 * @throws Exception
	 * @return xmlArray.toString()
	 */
	public String convertToXML(ResultSet resultSet) throws Exception {
		StringBuffer xmlArray = new StringBuffer("<results>");
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			xmlArray.append("<result ");
			for (int i = 0; i < total_rows; i++) {
				xmlArray.append(" " + resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase() + "='"
						+ resultSet.getObject(i + 1) + "'");
			}
			xmlArray.append(" />");
		}
		xmlArray.append("</results>");
		return xmlArray.toString();
	}
	
	/**
	 * Convert a Result JSONArray to a Vertices JSONArray
	 *
	 * @param JSONArray
	 * @throws Exception
	 * @return JSONArray with vertices
	 */
	public JSONArray convertToVJArray(JSONArray resJson) throws Exception {
		JSONArray verJson = new JSONArray();
		JSONObject tmpJson = new JSONObject();
		
		String[] eList = {"r_start","r_id","r_type","r_end"};
		
		for(int i=0;i<resJson.size();i++) {
			tmpJson = (JSONObject) resJson.get(i);
			for(int j=0; j<eList.length;j++) {
				tmpJson.remove(eList[j]);
			}
			verJson.add(tmpJson);
		}
		logger.info(verJson.toString());
		return verJson;
	}
	
	/**
	 * Convert a Result JSONArray to a edge JSONArray
	 *
	 * @param JSONArray
	 * @throws Exception
	 * @return JSONArray with vertices
	 */
	public JSONArray convertToEJArray(JSONArray resJson) throws Exception {
		JSONArray edJson = new JSONArray();
		JSONObject tmpJson = new JSONObject();
		
		String[] vList = {"s_id","s_props","e_id","e_props"};
		
		for(int i=0;i<resJson.size();i++) {
			tmpJson = (JSONObject) resJson.get(i);
			for(int j=0; j<vList.length;j++) {
				tmpJson.remove(vList[j]);
			}
			edJson.add(tmpJson);
		}
		
		return edJson;
	}
}
