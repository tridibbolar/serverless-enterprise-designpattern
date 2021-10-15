const AWS = require('aws-sdk')
AWS.config.region = process.env.AWS_REGION || 'us-east-1'
const eventbridge = new AWS.EventBridge()

module.exports.next_filter = (async function (event) {
    console.log("inside next_filter: ", event);
    var i = event.detail.filterlist.indexOf(event.detail.target);
    console.log(i);
    if (event.detail.filterlist.length === i + 1) {
        return null;
    } else {
        event.detail.target = event.detail.filterlist[i + 1];
        var finalEvent = {
            "Source": event.source,
            "EventBusName": "mypipe",
            "DetailType": event["detail-type"],
            "Time": new Date(),
            "Detail": JSON.stringify(event.detail, null, 2)
        }

        var Entries = [];
        Entries.push(finalEvent);
        var entry = { "Entries": Entries };
        console.log(JSON.stringify(entry, null, 2));
        var result = await eventbridge.putEvents(entry).promise();
        console.log(result);
        return result;
    }
});

module.exports.async_await = (async function (ms) {
    console.log("Wait time: ", ms);
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < ms);
});


