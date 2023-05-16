axios.defaults.baseURL = 'http://localhost:8080/mvc'

// 添加请求拦截器
axios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    let userid = sessionStorage.getItem('userid')
    if (userid != null) {
      config.headers.auth = userid
    }
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)
