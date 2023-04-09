import axios from 'axios'


// 请求超时时间
axios.defaults.timeout = 10000 * 5

// 请求基础 URL
axios.defaults.baseURL = '/api'

// POST 请求头
// axios.defaults.headers.post['Content-Type'] =
// 	'application/x-www-form-urlencoded'

// 请求携带cookie
axios.defaults.withCredentials = true


/**
 * 封装 get方法 对应 GET 请求
 * @param {string} url 请求url
 * @param {object} params 查询参数
 * @returns {Promise}
 */
export function get(url, params) {
	return new Promise((resolve, reject) => {
		axios
			.get(url, {
				params: params
			})
			.then((res) => {
				resolve(res.data)
			})
			.catch((err) => {
				reject(err.data)
			})
	})
}
/**
 * 封装 post 方法，对应 POST 请求
 * @param {string} url 请求url
 * @param {object} data 请求体
 * @param {boolean | undefined} info 请求体是否为 FormData 格式
 * @returns {Promise}
 */
export function post(url, data, info) {
	return new Promise((resolve, reject) => {

		if (!info) {
			axios
				.post(url, data,{
					headers:{
						'Content-type':'application/json'
					}
				})
				.then((res) => {
					resolve(res.data)
				})
				.catch((err) => {
					reject(err.data)
				})
		}else{
			axios
				.post(url, data,{
					headers:{
						'Content-type':'multipart/form-data'
					}
				})
				.then((res) => {
					resolve(res.data)
				})
				.catch((err) => {
					reject(err.data)
				})			
		}

	})	
	
}

