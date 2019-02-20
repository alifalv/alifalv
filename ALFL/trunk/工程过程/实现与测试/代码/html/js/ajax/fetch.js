var baseURL ='http://172.18.128.157:8888/api/';
// var baseURL ='http://m.test.cn-onsite.com/api/';
var api_server = axios.create({
  baseURL: baseURL,
  timeout: 5000,
});

api_server.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';

//请求拦截器
api_server.interceptors.request.use(function (config) {
    // 请求之前
    vue_loading.showLoading = true;
    /*if (token) {
        config.headers.Authorization = "Bearer"+' '+token;
    }*/
    if (config.method == 'post') {
        // 序列化
        // config.data = JSON.stringify(config.data)
    }
    return config
}, function (error) {
    console.log("错误的传参")
    return Promise.reject(error)
})

//响应拦截器
api_server.interceptors.response.use(function (res) {
    vue_loading.showLoading = false;
    return res.data;
}, function (error) {
    vue_loading.showLoading = false;
    /*console.log(error.response);
    var str = window.location.pathname;
    // 自己链接的地址，通过?prevUrl=xx 传给下一页
    var self_name = str.split("/").pop();
    if (error.response.status>399 && error.response.status<500) {
        if (error.response.status==401) {
            window.location.href="error401.html"
        } else {
            vue_loading.showLoading = false;
            vue_loading.$message.closeAll();
            vue_loading.$message({
                message: error.response.data,
                type: 'error',
                showClose: true,
                duration:0,
            });
        }
    }else if (error.response.status>=500) {
        window.location.href="error500.html?prevUrl=" + self_name
    }*/
    
})

get = function (url, para) {
    return new Promise(function (resolve, reject) {
        api_server.get( url , {params: para })
            .then(function (res) {
                resolve(res);
            }, function (err) {
                reject(err)
            })
            .catch(function (error) {
                reject(error)
            })
    })
};

post = function (url, params) {
    return new Promise(function (resolve, reject) {
        api_server.post(url, params)
            .then(function (response) {
                resolve(response)
            }, function (err) {
                reject(err)
            })
            .catch(function (error) {
                reject(error)
                console.log(error);
            })
    })
};




