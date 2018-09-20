package net.demo.modules.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;

import net.demo.modules.dto.HateoasDTO;

/**
 * Hateoas (RESTful API 규격 Level 3) configuration
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@Configuration
public class HateoasConfig {
	
	/**
	 * Data Transfer Object create
	 * 
	 * @return	Bean object return(HateoasDTO)
	 */
	@Bean(name="hateoas")
	public HateoasDTO hateoas() {
		return new HateoasDTO();
	}
	
	/**
	 * Resource 생성 by HateoasDTO
	 * 
	 * @return	resource return.
	 */
	@Bean(name="hateoasResource")
	public Resource<HateoasDTO> resource(@Qualifier("hateoas") HateoasDTO hd){
		return new Resource<HateoasDTO>(hd);
	}
}
