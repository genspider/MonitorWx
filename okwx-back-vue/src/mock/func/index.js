//得到 mock拦截的post 数据
function resetParmas(data) {
    var temp = data.body.split("&")
    var arr = {}
    for(var i =0,count = temp.length;i<count;i++){
        var arr2 = temp[i].split("=")
        arr[arr2[0]] = arr2[1]
    }
    return arr
}
export {resetParmas}