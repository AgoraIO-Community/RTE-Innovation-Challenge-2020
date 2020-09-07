'use strict';

/////////////////////////////////////////////////
// 对JSON的数据行列转换
/////////////////////////////////////////////////

var _this = this;

/**
 * 列转行，时间复杂度O(n*m)
 *
 * @param data Array JSON数组
 * @prarm dcol string 字段列名
 * @prarm dcol string 数据列名
 *
 * @example:
 * data = [
 * {"field": "date", "data": [20080102, 20080103, 20080104]},
 * {"field": "x1", "data": [1.5916, 2.5916, 3.5916]},
 * {"field": "x2", "data": [1.4916, 7.5916, 1.5916]}
 * ];
 * col2row(data)
 *
 * @output
 * [ { date: 20080102, x1: 1.5916, x2: 1.4916 },
 * { date: 20080103, x1: 2.5916, x2: 7.5916 },
 * { date: 20080104, x1: 3.5916, x2: 1.5916 } ]
 */
var col2row = function (data,ccol, dcol) {
    ccol = ccol || 'field';
    dcol = dcol || 'data';

    var rowLen = data[0][dcol].length;
    var rows = [];
    for (var i = 0; i < rowLen; i++) {
        var row = {}
        for (var j = 0; j < data.length; j++) {
            row[data[j][ccol]] = data[j][dcol][i];
        }
        rows.push(row);
    }
    return rows;
}

/**
 * 列转行, 时间复杂度O(n+m)
 *
 * @param data Array JSON数组
 * @prarm dcol string 字段列名
 * @prarm dcol string 数据列名
 */
var col2row2 = function (data, ccol, dcol) {
    ccol = ccol || 'field';
    dcol = dcol || 'data';

    var str = ''
        ,map = {}
        ,rowLen = 0
        ,i=0;

    for(i=0;i<data.length;i++){
        var field = data[i][ccol];
        map[field]=data[i][dcol];

        rowLen = map[field].length; // 数据的行数
        str += "obj['"+field+"'] =map['"+field+"'][i];" //构建行数的执行语句
    }

    var rows = [];
    for(i=0;i<rowLen;i++){
        var obj = {};
        eval(str)
        rows.push(obj);
    }
    return rows;
}

/**
 * 行转列
 *
 * @param data Array JSON数组
 * @prarm dcol string 字段列名
 * @prarm dcol string 数据列名
 *
 * @example:
 * data = [
 * { date: 20080102, x1: 1.5916, x2: 1.4916 },
 * { date: 20080103, x1: 2.5916, x2: 7.5916 },
 * { date: 20080104, x1: 3.5916, x2: 1.5916 }
 * ];
 * row2col(data)
 *
 * @output
 * [ { field: 'date', data: [ 20080102, 20080103, 20080104 ] },
 * { field: 'x1', data: [ 1.5916, 2.5916, 3.5916 ] },
 * { field: 'x2', data: [ 1.4916, 7.5916, 1.5916 ] } ]
 */
var row2col = function (data,ccol,dcol) {
    ccol = ccol || 'field';
    dcol = dcol || 'data';

    var keys = [],obj=data[0];
    for (var key in obj){
        if (hasOwnProperty.call(obj, key)) {
            keys.push(key);
        }
    }
    var cols = [];
    for(var i=0;i<keys.length;i++){
        var col = {};
        col[ccol]=keys[i]
        col[dcol]=[];

        for(var j=0;j<data.length;j++){
            col[dcol].push(data[j][keys[i]]);
        }
        cols.push(col);
    }
    return cols;
}




