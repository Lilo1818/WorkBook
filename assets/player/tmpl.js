/**
 * 微型模板引擎 tmpl
 * 方式：直接传入模板：(可以随心所欲的使用js原生语法)
 */

String.prototype.toDate = function (format) {
    if (this.indexOf("\/Date(") == 0) {
        var s = this.substring(6, this.length - 2);
        d = new Date(parseInt(s) - 8 * 60 * 60 * 1000);
        if (format) {
            return d.format(format);
        }
        return d;

    }
    return null;
}

Date.prototype.format = function (format) {

    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

var tmpl = (function (cache, $) {
    return function (str, data) {
        var fn = function (data) {
            var i, variable = [$], value = [[]];
            for (i in data) {
                variable.push(i);
                value.push(data[i]);
            };
            return (new Function(variable, fn.$))
            .apply(data, value).join("");
        };

        fn.$ = fn.$ || $ + ".push('"
        + str.replace(/\\/g, "\\\\")
                 .replace(/[\r\t\n]/g, " ")
                 .split("<#").join("\t")
                 .replace(/((^|#>)[^\t]*)'/g, "$1\r")
                 .replace(/\t=(.*?)#>/g, "',$1,'")
                 .split("\t").join("');")
                 .split("#>").join($ + ".push('")
                 .split("\r").join("\\'")
        + "');return " + $;

        return data ? fn(data) : fn;
    }
})({}, '$' + (+new Date));

tmpl.render = function (id, data) {  
    var t = document.getElementById(id).innerHTML;    
    var c = tmpl(t, data);
    return c;
}