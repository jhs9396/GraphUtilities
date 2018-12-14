var path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: {
    	app: './src/main/resources/static/js/app.js',
//    	'webpack-dev-server/client?http://localhost:8082'
//    	graph: './src/main/resources/static/js/veridato/modules/graph.js',
//    	async: './src/main/resources/static/js/veridato/modules/async.js',
//    	index: './src/main/resources/static/js/veridato/modules/index.js'
    },
    devtool: 'sourcemaps',
    cache: true,
    output: {
        path: __dirname,
//        filename: './src/main/resources/static/build/[name].js'
        filename: './src/main/resources/static/build/bundle.js'
    },
    resolve: {
    	alias: {
    		contents: path.resolve(__dirname, 'src/main/resources/static/js/contents'),
    		modules:  path.resolve(__dirname, 'src/main/resources/static/js/modules')
    	}
    },
    module: {
        rules: [
        	{
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel-loader',
                query: {
                    cacheDirectory: true,
                    presets: ['env', 'react']
                }
            },
        	{
            	test: __dirname+"/src/main/resources/static/images/\.(png|jpg|gif)$\i",
            	use: [
            		{
            			loader: 'url-loader',
            			options: {
            				limit: 8192
            			}
            		}
            	]
        	},
        	{
        		test: __dirname+"/src/main/resources/static/css/\.css$/",
        		use: [ {loader: 'style-loader'}, {loader: 'css-loader'}]
        	}
        ]
    }
};