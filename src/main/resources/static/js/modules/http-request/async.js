/**
 * import component 
 */
import axios from 'axios'

/**
 * Async function get, post by axios
 * response data(response.data)
 * response.data.embedded => return promise resolve data 
 * 
 * 
 * @author HyeonSu Jeon 
 */
const set = (response) => {
	return response
	.then((response) => (JSON.parse(JSON.stringify(response.data.embedded))))
	.catch(err => {
		console.log('reject issue >> ', err)
	})
}

// async call method get 
export const get = (url, params) => (set(axios.get(url,params)))

// async call method post
export const post = (url, params) => (set(axios.post(url,params)))