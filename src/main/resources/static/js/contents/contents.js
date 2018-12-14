/**
 * Content list component
 */
import React  from 'react'
import {Link} from 'react-router-dom'
import {hot} from 'react-hot-loader'
//import {FontAwesomeIcon} from 'react-fontawesome'
//import 'w3-css/w3.css'
/**
 * Contents component
 * left : nav list 
 * right : content div 
 * 
 */
class Contents extends React.Component {
	// constructor
	constructor(props){
		super(props)
		console.log('this.props', this.props)
		this.state = {
			
		}
	}
	
	w3_open() {
		if (mySidebar.style.display === 'block') {
	        mySidebar.style.display = 'none';
	        overlayBg.style.display = "none";
	    } else {
	        mySidebar.style.display = 'block';
	        overlayBg.style.display = "block";
	        mySidebar.style.width = '25%';
	        content.style.leftMargin = '25%';
	    }
	}
	
	w3_close() {
	    mySidebar.style.display = "none";
	    overlayBg.style.display = "none";
	    
	}
	
	render(){
		// nav list style
		let navStyle = {
				zIndex:3,
				width:'250px'
		}
		
		// account image style
		let imgStyle = {
				width:'46px'
		}
		
		// top area style
		let topStyle = {
				zIndex:4
		}
		
		// content div style
		let contentStyle = {
				marginLeft:'250px',
				marginTop: '42px'
		}
		
		// {Context Graph, Cluster Graph, Result Table Attacker Group, Attack Pattern, Campaign} Style
		let linkStyle = {
				textDecoration: 'none',
				display: 'block'
		}
		
		let footerStyle = {
				zIndex:2,
				height:'40px'
		}
		
		// Menu text list
		let menuList = [
			{icon:'fa fa-diamond fa-fw', name:'Home'}
		]
		
		// rendering
		return (
			<div>
				<div id="contents-top-div" className="w3-bar w3-top w3-black w3-large" style={topStyle}>
					<button className="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey" onClick={this.w3_open}><i className="fa fa-bars"></i>  Menu</button>
				  	<img className="w3-bar-item w3-right" src="/images/logo/logo_bitnine_100_w.png"></img>
				</div>
				<nav className="w3-sidebar w3-collapse w3-white w3-animate-left" style={navStyle} id="mySidebar"><br></br>
					<div className="w3-container w3-row">
						<div className="w3-col s4">
				    		<img src="/images/icon/avatar2.png" className="w3-circle w3-margin-right" style={imgStyle}></img>
			    		</div>
			    		<div className="w3-col s8 w3-bar">
				    		<span>Welcome, <strong>Mike</strong></span><br></br>
				    		<a href="#" className="w3-bar-item w3-button"><i className="fa fa-envelope"></i></a>
				    		<a href="#" className="w3-bar-item w3-button"><i className="fa fa-user"></i></a>
				    		<a href="#" className="w3-bar-item w3-button"><i className="fa fa-cog"></i></a>
				    	</div>
				    </div>
				    <hr></hr>
				    <div className="w3-container">
				    	<h5>Menu</h5>
				    </div>
				    <div className="w3-bar-block">
					    <a href="#" className="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black" onClick={this.w3_close} title="close menu">
					    	<i className="fa fa-remove fa-fw"></i>  Close Menu
					    </a>
					    {menuList.map((value, index) => {
					    	let path = this.props.children.props.path
					    	let link = <Link to={path} style={linkStyle}><span className="w3-bar-item w3-button w3-padding"><i className={value.icon}></i>  {value.name}</span></Link>
					    	return <div>{link}</div>
					    })}
					    <br></br><br></br>
				    </div>
				</nav>
				<div id="content" className="w3-main w3-container" style={contentStyle}>
					{this.props.children}
				</div>
				
				<div id="contents-footer-div" className="w3-bar w3-bottom w3-dark-grey w3-large" style={footerStyle}>
					<div className="w3-bar-item w3-text-white w3-right">
						Powered by <a href="https://github.com/jhs9396/GraphUtilities">Jeon HyeonSu</a>
					</div>
				</div>
				
			</div>
		)
	}
}

export default Contents