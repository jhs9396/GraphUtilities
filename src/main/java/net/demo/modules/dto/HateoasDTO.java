package net.demo.modules.dto;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Map;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * Level3. Hateoas Data Transfer Object
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
public class HateoasDTO {
	
	/**
	 * parameters field
	 */
	Map<?,?> parameters;
	
	/**
	 * result field
	 */
	String embedded;
	
	/**
	 * request parameters list getter
	 * 
	 * @return	request 되었던 parameters를 가져온다
	 */
	public Map<?, ?> getParameters() {
		return parameters;
	}
	
	/**
	 * request된 parameters setter
	 * 
	 * @param	parameters	request parameters
	 */
	public void setParameters(Map<?, ?> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * JSONArray to String된 JDBC Query execute result getter 
	 * 
	 * @return	String으로 변환된 result를 가져온다.
	 */
	public String getEmbedded() {
		return embedded;
	}
	
	/**
	 * result setter
	 * bitnine.JSONArray, JSONObject binding error가 발생하므로 String으로 casting된 값을 가지고 있는다.
	 * 
	 * @param	embedded	String으로 변환된 result variable
	 */
	public void setEmbedded(String embedded) {
		this.embedded = embedded;
	}
	
	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();
		
		text.append("[");
		if(embedded != null) text.append("embedded="+embedded);
		text.append("]");
		return text.toString();
	}
	
	/**
	 * Hateoas self link add
	 * 
	 * @param	resource	Hateoas resource object reference
	 * @param	methodOn	static method // methodOn(this.getClass()).{method name(parameters)}
	 * @return	참조된 resource 객체에 URI selflink를 추가한다.
	 */
	public <T> void selfLink(Resource<?> resource, T methodOn) {
		ControllerLinkBuilder linkTo = linkTo(methodOn);
		resource.removeLinks();
		resource.add(linkTo.withRel("selfLink"));
	}
}
