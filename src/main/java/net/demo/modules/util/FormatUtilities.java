package net.demo.modules.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;
import net.demo.modules.logger.UDLogger;

/**
 * @author HyeonSu Jeon
 * @version 0.2
 * @since 0.1
 */
@Component
public class FormatUtilities {
	@UDLogger Logger logger;
	/**
	 * Convert a ResultSet into a JSONArray
	 *
	 * @param ResultSet
	 * @throws SQLException,JSONException
	 * @return JSONArray
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
