package net.demo.modules.services;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
 * @version	0.2
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
	
	@Value("${spring.datasource.graphpath}")
	String graphPath;

	@Value("${spring.datasource.username}")
	String schemaName;

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
		StringBuffer query = new StringBuffer();
		query.append(" MATCH (a)-[r]->(b) RETURN a,r,b ");
		
		JSONObject result = qt.doGraphQuery(query.toString());
		
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
	
	/**
	 * AgensGraph Meta graph search service method
	 * 
	 * @return	d3.js data return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAgMetaGraph() {
		JSONObject resJson = new JSONObject();
		// If AgensGraph v2.0 ↑, below query is executing. but ag_graphmeta_view takes a bug. (ag_graphmeta information do not sync.)
		qt.query.append(" SELECT "
				      + "         relname AS id "
				      + "    ,    n_live_tup AS count "
				      + " FROM    pg_stat_all_tables "
				      + " WHERE   schemaname = ? "
				      + " AND     relid IN (SELECT relid FROM pg_catalog.ag_label WHERE labkind = 'v' ) "
				      + " AND     relname IN (SELECT b.start FROM ag_graphmeta_view b WHERE b.graphname = ? "
				      + "                     UNION "
				      + "                     SELECT a.end FROM ag_graphmeta_view a WHERE a.graphname = ?) ");
		qt.paramList.put("schemaname", schemaName);
		qt.paramList.put("graphname1", graphPath);
		qt.paramList.put("graphname2", graphPath);

        // If you are used AgensGraph 1.3~2.0 under version, below query is not executed.
		// reason : cypher query RETURN ty pe all column, object is 'jsonb' -> if you search graphid_labid, graphid_labid-> text, but after RETURN logic, graphid_labid type is 'text'->'jsonb'
		// Hybrid query used,  you are able to use PostgreSQL type(e.g ::int, ::text)
		// executed version : AgensGraph 2.0, 2.1 version
/*		qt.query.append(" SELECT "
				 +   "    t1.s_oid AS s_id "
				 +   " ,  (SELECT labname FROM pg_catalog.ag_label WHERE labid = t1.s_oid::int) AS s_label "
				 +   " ,  ROW_NUMBER() OVER (ORDER BY t1.e_oid) AS e_id "
				 +   " ,  (SELECT labname FROM pg_catalog.ag_label WHERE labid = t1.e_oid::int) AS e_label "
				 +   " ,  t1.t_oid AS t_id "
				 +   " ,  (SELECT labname FROM pg_catalog.ag_label WHERE labid = t1.t_oid::int) AS t_label "
				 +   " FROM "
				 +   "    ( MATCH (a)-[r]->(b) "
				 +   "      WHERE label(a) <> 'ag_vertex' "
				 +   "      AND   label(b) <> 'ag_vertex' "
				 +   "      RETURN DISTINCT graphid_labid(start(r)) as s_oid, graphid_labid(id(r)) as e_oid, graphid_labid(\"end\"(r)) as t_oid "
				 +   "    ) t1 ");*/

		JSONArray nodes = qt.doQuery(qt.query.toString(), qt.paramList);
		qt.query.append(" SELECT start as source, \"end\" as target , edge, edgecount "
				      + " FROM ag_graphmeta_view "
				      + " where graphname = ? ") ;
		qt.paramList.put("graphname", graphPath);

		JSONArray edges = qt.doQuery(qt.query.toString(), qt.paramList);

		resJson.put("nodes", nodes);
		resJson.put("links", edges);
		return resJson;
	}
}
