const util = require('/opt/nodejs/utility');
'use strict';

exports.handler = async (event, context, callback) => {
    console.log('filter-1');
    //util.async_await(300000);
    console.log('Received event:', JSON.stringify(event, null, 2));
    var jo = event.detail;
    console.log(jo);
    const response = util.next_filter(event);
    console.log (response);
    callback(null, 'Finished');
};
