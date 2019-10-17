import {post,get,put} from "../utils/http";

//var api = process.env.API2
var api = "http://10.200.52.183:9090"
export const invoice = p => put(api+'/invoice', p);

export const invoicelog = p => get(api+'/invoicelog', p);