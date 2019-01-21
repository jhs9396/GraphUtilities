package net.demo.modules.services.db;

import net.bitnine.agensgraph.deps.org.json.simple.JSONArray;
import net.bitnine.agensgraph.deps.org.json.simple.JSONObject;
import net.demo.modules.logger.UDLogger;
import net.demo.modules.util.FormatUtilities;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * AgensGraph QueryTemplate by JdbcTemplate
 * @author HyeonSu Jeon
 * @version 0.1
 * @since 0.1
 */
@Service
public class QueryTemplate {
	/**
	 * JdbcTemplate 객체 호출
	 */
	JdbcTemplate jdbcTemplate;
	
	/**
	 * Query ResultSet format 전용 객체 호출
	 */
	FormatUtilities fu;
	
	/**
	 * slf4j logger
	 */
	@UDLogger Logger logger;
	
	/**
	 * ANSI-SQL or Cypher query String buffer
	 */
	public StringBuffer query;
	
	/**
	 * parameter list object
	 */
	public Map<String,Object> paramList;
	
	@Autowired
	public QueryTemplate(FormatUtilities fu, @Qualifier("JdbcTemplate")JdbcTemplate jdbcTemplate) {
		this.fu           = fu;
		this.jdbcTemplate = jdbcTemplate;
		this.query        = new StringBuffer();
		this.paramList    = new LinkedHashMap<String,Object>();
	}
	
	/**
	 * statement Read용 쿼리 실행 Method 
	 *
	 * @param  query 		QueryFactory에서 전달받는 query
	 * @throws Exception	JSONArray 생성 시 오류가 나는 경우
	 * @return ResultSet을 JSONArray로 변경하여 전달
	 */
	@Cacheable
	public JSONArray doQuery(String query) {
//		String query = "MATCH (a) RETURN a";
		
		JSONArray resJson = jdbcTemplate.query(query, new ResultSetExtractor<JSONArray>() {
			@Override
			public JSONArray extractData(ResultSet rs) throws SQLException, DataAccessException {
				JSONArray ed = new JSONArray();
				try {
					ed =  fu.convertToJSONArray(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ed;
			}
		});
		logger.debug(resJson.toJSONString());
		initQuery();
		
		return resJson;
	}
	
	/**
	 * statement Read용 쿼리 실행 Method 
	 *
	 * @param  query 		QueryFactory에서 전달받는 query
	 * @throws Exception	JSONArray 생성 시 오류가 나는 경우
	 * @return ResultSet을 JSONArray로 변경하여 전달
	 */
	@Cacheable
	public JSONObject doGraphQuery(String query) {
		JSONObject resJson = jdbcTemplate.query(query, new ResultSetExtractor<JSONObject>() {
			@Override
			public JSONObject extractData(ResultSet rs) throws SQLException, DataAccessException {
				JSONObject ed = new JSONObject();
				try {
					ed =  fu.convertToGraphObj(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ed;
			}
		});
		
		logger.debug(resJson.toString());
		initQuery();
		
		return resJson;
	}
	
	/**
	 * PreparedStatement Read용 쿼리 실행 Method 
	 *
	 * @param  query 				QueryFactory에서 전달받는 query
	 * @param  paramList			Query parameters
	 * @throws SQLException 		Query 문법/실행 에러
	 * @throws DataAccessException	ResultSet DataAccess 시 에러 발생할 때	
	 * @return ResultSet을 JSONArray로 변경하여 전달
	 */
	@Cacheable
	public JSONObject doGraphQuery(String query, Map<String,?> paramList) {
		JSONObject resJson = jdbcTemplate.query(query,
			new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int idx=1;
					if(paramList != null) {
						Set<? extends Map.Entry<String, ?>> keys = paramList.entrySet();
						for(Map.Entry<String, ?> entry : keys){
							ps.setObject(idx++, entry.getValue());
						}
					}
					ps.execute();
				}
			},		
	        new ResultSetExtractor<JSONObject>() {
				@Override
				public JSONObject extractData(ResultSet rs) throws SQLException, DataAccessException {
					JSONObject ed = new JSONObject();
					try {
						ed =  fu.convertToGraphObj(rs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ed;
				}
			});
		
		logger.debug(resJson.toString());
		initQuery();
		
		return resJson;
	}
	
	/**
	 * PreparedStatement Read용 쿼리 실행 Method 
	 *
	 * @param  query 				QueryFactory에서 전달받는 query
	 * @param  paramList			Query parameters
	 * @throws SQLException 		Query 문법/실행 에러
	 * @throws DataAccessException	ResultSet DataAccess 시 에러 발생할 때	
	 * @return ResultSet을 JSONArray로 변경하여 전달
	 */
	@Cacheable
	public JSONArray doQuery(String query, Map<String,?> paramList) {  
//		String query = "MATCH (a) RETURN a";
		JSONArray resJson = jdbcTemplate.query(query, 
			new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int idx=1;
					if(paramList != null) {
						Iterator<String> keys = paramList.keySet().iterator();
						while(keys.hasNext()) {
							String key = keys.next();
							ps.setObject(idx++, paramList.get(key));
						}
					}
					ps.execute();
				}
			},
			new ResultSetExtractor<JSONArray>() {
				@Override
				public JSONArray extractData(ResultSet rs) throws SQLException, DataAccessException {
					JSONArray ed = new JSONArray();
					try {
						ed =  fu.convertToJSONArray(rs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ed;
				}
			});
		logger.debug(resJson.toJSONString());
		initQuery();

		return resJson;
	}
	
	/**
	 * Read용 Paging Query
	 * 
	 * @param  query 				SQL모양으로 가져와야 페이징 처리 가능
	 * @param  page					page 숫자
	 * @param  rows					rows 숫자
	 * @throws SQLException 		Query 문법/실행 에러
	 * @throws DataAccessException	ResultSet DataAccess 시 에러 발생할 때	
	 * @return ResultSet 결과 JSONArray로 변환하여 전달
	 */
	@Cacheable
	public JSONArray doPagingQuery(String query, int page, int rows) {
		int offset = ( page - 1 ) * rows;
		
		// SQL에 한하여
//		String query = 	"SELECT * FROM TABLE";
		query += 		" OFFSET " + offset + " LIMIT " + rows;
	
		JSONArray resJson = jdbcTemplate.query(query,new ResultSetExtractor<JSONArray>() {
				@Override
				public JSONArray extractData(ResultSet rs) throws SQLException, DataAccessException {
					JSONArray ed = new JSONArray();
					try {
						ed =  fu.convertToJSONArray(rs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ed;
				}
			});
		logger.debug(resJson.toJSONString());
		return resJson;
	}
	
	/**
	 * Read용 Paging Query
	 * 
	 * @param  query 				SQL모양으로 가져와야 페이징 처리 가능
	 * @param  paramList			Query parameters
	 * @param  page					page 숫자
	 * @param  rows					rows 숫자
	 * @throws SQLException 		Query 문법/실행 에러
	 * @throws DataAccessException	ResultSet DataAccess 시 에러 발생할 때	
	 * @return ResultSet 결과 JSONArray로 변환하여 전달
	 */
	@Cacheable
	public JSONArray doPagingQuery(String query, Map<String,?> paramList, int page, int rows) {
		int offset = ( page - 1 ) * rows;
		
		// SQL에 한하여
//		String query = 	"SELECT * FROM TABLE";
		query += 		" OFFSET " + offset + " LIMIT " + rows;
	
		JSONArray resJson = jdbcTemplate.query(query, 
			new PreparedStatementSetter() {
		
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int idx=1;
					if(paramList != null) {
						Iterator<String> keys = paramList.keySet().iterator();
						while(keys.hasNext()) {
							String key = keys.next();
							ps.setObject(idx++, paramList.get(key));
						}
					}
					ps.execute();
				}
			}
			,new ResultSetExtractor<JSONArray>() {
				@Override
				public JSONArray extractData(ResultSet rs) throws SQLException, DataAccessException {
					JSONArray ed = new JSONArray();
					try {
						ed =  fu.convertToJSONArray(rs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ed;
				}
			});
		logger.debug(resJson.toJSONString());
		return resJson;
	}
	
	/**
	 * Create / Update / Delete 전용 Method(w/o parameters) 
	 * 
	 * @param  query				DDL 문법 Query
	 * @throws SQLException			Query 문법/실행 에러
	 * @throws DataAccessException 	PreparedStatement에 맞지 않는 타입이 전달되었을 때
	 * @return Insert/Update 된 Row의 갯수
	 */
	
	public Integer doUpdate(String query) {
		//		String query = "CREATE (a:person {id:1})";
		
		return jdbcTemplate.update(query);
		
	}
	
	/**
	 * Create / Update / Delete 전용 Method 
	 * 
	 * @param  query				DDL 문법 Query
	 * @param  paramList			Controller에서 전달받는 parameters 구조체
	 * @throws SQLException			Query 문법/실행 에러
	 * @throws DataAccessException 	PreparedStatement에 맞지 않는 타입이 전달되었을 때
	 * @return true/false로  execute 여부 체크
	 */
	
	public Boolean doExecute(String query, Map<String,?> paramList) {
//		String query = "CREATE (a:person ?)";
		
		return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				int idx=1;
				if(paramList != null) {
					Iterator<String> keys = paramList.keySet().iterator();
					while(keys.hasNext()) {
						String key = keys.next();
						ps.setObject(idx++, paramList.get(key));
					}
				}
				return ps.execute();
			}
			
		});
	}
	
	/**
	 * CallableStatement용 생성 쿼리. function, procedures, cursors 이용 시 callablestatement로 파라메터를 설정해야 한다.
	 * 
	 * @param	variable	Description text
	 * @throws	exception_name	Description
	 * @return	Description text
	 */
	public Integer doCallExecute(String query, Map<String,?> paramList) {
//		String query = "CREATE (a:person ?)";
		
		return jdbcTemplate.execute(query, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement ps) throws SQLException, DataAccessException {
				int idx=1;
				if(paramList != null) {
					Iterator<String> keys = paramList.keySet().iterator();
					while(keys.hasNext()) {
						String key = keys.next();
						ps.setObject(idx++, paramList.get(key));
					}
				}
				return ps.executeUpdate();
			}
			
		});
	}
	
	/**
	 * 다 수의 list를 업데이트 하기 위한 BatchUpdate method이며, 비동기식으로 작동
	 *  
	 * @param <T> 		사용자가 명시한 Object type
	 * @param  query 	QueryFactory에서 전달받는 query
	 * @param  list		사용자가 명시한 Object의 list - parameters	
	 * @return size array return
	 */
	@Async
	public <T> int[] doBatchQuery(String query, List<?> list) {  
		
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, list.get(i));
			}
		});

	}
	
	/**
	 * 비동기 query 수행
	 * 단, 비동기 query가 수행했는지 체크할 수 있게 completableFuture type으로 리턴
	 * 
	 * @param	query	질의 수행할 query문
	 * @param	list	batchQuery를 수행하기 위한 리스트
	 * @return	CompletableFuture 객체타입 리턴, 비동기성 쿼리의 수행을 체크하기 위함
	 */
	@Async
	public <T> CompletableFuture<int[]> doAsyncQuery(String query, List<?> list) {  
		
		return CompletableFuture.completedFuture(jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, list.get(i));
			}
		}));

	}
	
	/**
	 * StringBuffer and Map object initializing method
	 * 
	 */
	public void initQuery() {
		this.query.delete(0, this.query.length());
		this.paramList.clear();
	}
}
