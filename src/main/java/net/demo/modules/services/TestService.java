package net.demo.modules.services;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;
import net.demo.modules.logger.UDLogger;
import net.demo.modules.services.db.QueryTemplate;
import net.demo.modules.util.GraphUtilities;

/**
 * AgensGraph Test service
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@Service
public class TestService {
	
	/**
	 * slf4j logger
	 */
	@UDLogger Logger logger;
	
	/**
	 * GraphUtilities 객체
	 */
	GraphUtilities gu;
	
	/**
	 * QueryTemplate 객체
	 */
	QueryTemplate qt;
	
	/**
	 * SimpleDateformat 연-월-일 시:분:초
	 */
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Injection Constructor
	 * 
	 */
	@Autowired
	public TestService(@Qualifier("GraphUtil")GraphUtilities gu, @Qualifier("QueryTemplate")QueryTemplate qt) {
		this.gu = gu;
		this.qt = qt;
	}
	
	/**
	 * Graph 조회 ---> GraphUtilities를 이용하여 포맷 설정
	 * 
	 * @param	not	하지만 화면에서 parameters를 넘겨서 처리 가능
	 * @return	그래프 format에 데이터 입력 후 넘겨주기
	 */
	public JSONArray getData() {
		JSONArray   resJson = new JSONArray();             // result graph data를 담는 객체
		StringBuffer  query = new StringBuffer();          // query를 작성하는 StringBuffer
				
		query.append(" MATCH (a)-[r]->(b) "
				   + " RETURN id(a) AS a_id, label(r) AS r_label, id(b) AS b_id ");
		
		JSONArray qryRslt = qt.doQuery(query.toString());  // query 실행
		
		// graph data setting by GraphUtilities
		for(int i=0; i<qryRslt.size(); i++) {
			String source_id = ((JSONObject)qryRslt.get(i)).get("a_id").toString();    // source vertex 
			String r_label   = ((JSONObject)qryRslt.get(i)).get("r_label").toString(); // edge
			String target_id = ((JSONObject)qryRslt.get(i)).get("b_id").toString();    // target vertex
			
			// nodes setting
			gu.node(source_id, "source"+i);
			gu.node(target_id, "target"+i);
			
			// edge setting
			gu.edge("rel"+i, r_label, source_id, target_id);
		}
		
		resJson = gu.toJsonArray();                         // result add JSONArray
		logger.info("resJson >> "+resJson.toJSONString()); 
		gu.graphInit();                                     // graph data initialize
		
		return resJson;
	}
	
	/**
	 * Graph data Neo4j formatting test service method
	 * 
	 * @return	cytoscape.js graph format JSONArray return
	 */
	public JSONArray getNeo4jFormatTest() {		
		qt.query.append(" MATCH (a)-[r]->(b) RETURN a,r,b ");
		
		JSONObject result = qt.doGraphQuery(qt.query.toString());
		
		JSONArray nodes = ((JSONArray)((JSONObject)result.get("graph")).get("nodes"));
		JSONArray edges = ((JSONArray)((JSONObject)result.get("graph")).get("edges"));
		JSONArray etc   = ((JSONObject)result.get("graph")).get("etc") != null ? ((JSONArray)((JSONObject)result.get("graph")).get("etc")) : new JSONArray();
		
		for(int i=0; i<nodes.size(); i++) {
			gu.node(((JSONObject)nodes.get(i)).get("id"), 
					i,
					((JSONObject)nodes.get(i)).get("label"),
					"node");
		}
		
		for(int i=0; i<edges.size(); i++) {
			gu.edge(((JSONObject)edges.get(i)).get("id"),
					((JSONObject)edges.get(i)).get("label"), 
					((JSONObject)edges.get(i)).get("startNode"), 
					((JSONObject)edges.get(i)).get("endNode"), 
					"edge");
		}
		
		for(int i=0; i<etc.size(); i++) {
			// nothing
		}
		
		logger.info("getNeo4jFormattest result >> "+result);
		
		return gu.toJsonArray();
	}
}
