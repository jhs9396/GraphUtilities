/**
 * import component 
 */
import React from 'react'

/**
 * Graph Component module
 * jsDoc으로 작성할 예정 틀 구성 후 공유
 * 
 * @author HyeonSu Jeon 
 */
class Graph extends React.Component {
	/**
	 * constructor and state initialize
	 * state↓
	 * style       Graph nodes, edges 기타 등등 style
	 * extension   GraphWidget extension 정의
	 *  
	 * @param props Parent component(contents) properties
	 */ 
	constructor(props){
		super(props)
		this.state = {
			style: [],
			extension: {}
		}
	}
	
	componentWillMount() {
		
	}
	
	// after component mounting(rendering) 
	componentDidMount() {
	}
	
	// after component state setting
	componentDidUpdate() {
		console.log('parent component state changging!')
		this.initGraph(this.setExtension())
	}
	
	componentWillUnmount(){
	}
	
	// Can be edited graphwidget extension 
	setExtension(){
		return {
			extension:{
                panzoom: {
                    minZoom: 0.1,   // min zoom level
                    maxZoom: 3,     // max zoom level
                    fitPadding: 70  // padding when fitting
                }
			}
		}
	}
	
	// Graph visualization object create
	initGraph(extension) {
		console.log('graph.js this.props.data', this.props.data)
		var GW = GraphWidget.GraphWidget
		var gw = new GW(document.getElementById('graph'), extension)
		
		/** Can be added the graph layout setting area **/
		gw.doLayout()
		
		/** Can be added to the graph data **/
		gw.add(this.props.data)
		
		/** Setting graphwidget object reference **/
		this.props.gw !== undefined?this.props.gw(gw):null
	}
	
	// component rendering
	render(){
		return (
			<div>
				<div id="graph" style={this.props.style} />
			</div>
		)
	}
}

export default Graph