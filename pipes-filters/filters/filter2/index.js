const util = require('/opt/nodejs/utility');
'use strict';

exports.handler = (event, context, callback) => {
    console.log('filter-2');
    //util.async_await(300000);
    console.log("now next!");
    console.log('Received event:', JSON.stringify(event, null, 2));
    var jo = event.detail;
    console.log(jo);
    const response = util.next_filter(event);
    console.log (response);
    callback(null, 'Finished');
};
