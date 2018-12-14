/**
 * ReactDOM Object
 */
'use strict';

/**
 * import component by React v4
 */
import React from 'react'
import ReactDOM from 'react-dom'
import {Route, Switch, BrowserRouter as Router} from 'react-router-dom'
import {Contents, Home} from './contents'

ReactDOM.render((
		// Component routing
		<Router>
			<Contents >
				<Route exact path="/main"     component={Home} />
			</Contents>
		</Router>
		),document.getElementById('contents')
)

if (module.hot) {
	module.hot.accept();
}