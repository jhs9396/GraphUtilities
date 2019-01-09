package net.demo.modules.controller;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.demo.modules.dto.HateoasDTO;
import net.demo.modules.logger.UDLogger;
import net.demo.modules.services.TestService;


/**
 * Graph Utilities test rest controller
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@RestController
@ExposesResourceFor(HateoasDTO.class)
@RequestMapping(value="/api/graph", produces="application/hal+json; charset=utf-8")
public class GraphUtilController {
	
	/**
	 * slf4j logger
	 */
	@UDLogger Logger logger;
	
	/**
	 * application.properties 값 
	 */
	@Value("${spring.project.name}")
	private String productName;

	/**
	 * application.properties 값 
	 */
	@Value("${spring.project.version}")
	private String productVersion;
	
	/**
	 * Hateoas Data response용 Transfer Object
	 */
	private HateoasDTO hd;
	
	/**
	 * Hateoas resource object
	 */
	private Resource<HateoasDTO> resource;

	/**
	 * Description
	 */
	private TestService ts;
	
	/**
	 * ResponseEntity Header 선언
	 *
	 * @return HTTP Header 명시
	 */
	private final HttpHeaders productHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("spring.project.name",    productName);
		headers.add("spring.project.version", productVersion);
		return headers;
	}
	
	/**
	 * Constructor
	 * 
	 * @param	ts       TestService injection용 객체
	 * @param	hd       HateoasDTO bean 객체
	 * @param	resource Hateoas 규격 객체
	 */
	@Autowired
	public GraphUtilController(TestService ts, @Qualifier("hateoas")HateoasDTO hd, @Qualifier("hateoasResource")Resource<HateoasDTO> resource) {
		this.ts       = ts;
		this.hd       = hd;
		this.resource = resource;
	}
	
	/**
	 * graph data 기본 조회용 컨트롤러
	 *
	 * @param  params	화면에서 넘어온 request parameters
	 * @param  model	화면에 넘길 response parameters
	 * @return Graph Data return(Nodes, Edges)
	 */
	@RequestMapping(value="/data",method=RequestMethod.GET)
	public ResponseEntity<Resource<HateoasDTO>> getGraphData(@RequestParam Map<String,Object> params){
		logger.debug("[getGraphData] Input: "+params);
		
		hd.setParameters(params);
		hd.setEmbedded(ts.getNeo4jFormatTest());
		hd.selfLink(resource, methodOn(this.getClass()).getGraphData(params));
		
		logger.debug("[getGraphData] Output: "+resource);
		
		return new ResponseEntity<Resource<HateoasDTO>> (resource,productHeaders(),HttpStatus.OK);
	}
}
