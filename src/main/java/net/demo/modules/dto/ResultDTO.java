package net.demo.modules.dto;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;


/**
 * Short one line description
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@SuppressWarnings("unchecked")
public class ResultDTO {
	private final String type="cypher";
	private String format_version="0.1";
	private Integer qtime;
	private JSONArray rows;
	
	public String getType() {
		return type;
	}
	
	
	public String getFormat_version() {
		return format_version;
	}
	
	public void setFormat_version(String format_version) {
		this.format_version = format_version;
	}
	
	public Integer getQtime() {
		return qtime;
	}
	
	public void setQtime(Integer qtime) {
		this.qtime = qtime;
	}
	
	public JSONArray getRows() {
		return rows;
	}
	
	public void setRows(JSONArray rows) {
		this.rows = rows;
	}


	@Override
	public String toString() {
		return "Format [type=" + type + ", format_version=" + format_version + ", qtime=" + qtime + ", rows="
				+ rows + "]";
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
        json.put("type",           type);
        json.put("format_version", format_version);
        json.put("qtime",          qtime);
        json.put("rows",           rows);
        return json;
	}
}
