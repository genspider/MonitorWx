/***
 * 封装 缓存
 * @param key
 * @param val
 */

function saveObj(key,val) {
    sessionStorage.setItem(key,JSON.stringify(val))
}
function getObj(key) {
    return JSON.parse(sessionStorage.getItem(key));
}
function removeObj(key) {
    sessionStorage.removeItem(key)
}
export {saveObj,getObj,removeObj}