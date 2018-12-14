/**
 * Home component
 */

import React from 'react'
import Graph from 'modules/visualization/graph/graph'

class Home extends React.Component {
	constructor(props) {
		super(props)
		this.handleResize = this.handleResize.bind(this)
		this.state = {
			style : {
				height: undefined
			}
		}
	}
	
	handleResize(){
		this.setState({
			style: {
				height: window.innerHeight-160
			}
		})
	}
	
	componentWillMount() {
		this.handleResize()
	}
	
	componentDidMount() {
		window.addEventListener('resize', this.handleResize)
	}
	
	componentWillUnmount(){
	    window.removeEventListener('resize', this.handleResize)
	}
	
	getElements() {
		return (
			[
				{group:'nodes', data: { id: 'a'}},
				{group:'nodes', data: { id: 'b'}},
				{group:'nodes', data: { id: 'd'}},
				{group:'nodes', data: { id: 'e'}},
				{group:'edges', data: { id: 'ad', source: 'a', target: 'd' } },
				{group:'edges', data: { id: 'eb', source: 'e', target: 'b' } }
			
			]
		)
	}
	
	render() {
		return (
			<div>
				<div><h2><i className="fa fa-arrow-circle-right"></i> Home !!!</h2></div>
				<Graph data={this.getElements()} style={this.state.style}/>
			</div>
		)
	}
} 

export default Home