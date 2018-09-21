package net.demo.modules.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.demo.modules.util.GraphUtilities;

/**
 * GraphUtilities Configuration
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@Configuration
public class GraphConfig {

	/**
	 * create graphutilities bean object
	 * 
	 * @return	GraphUtilities object return. (Bean)
	 */
	@Bean(name="GraphUtil")
	public GraphUtilities createGraphObject() {
		return new GraphUtilities();
	}
	
}
