# Utility file - deploy as lambda layer

## Modules
* **next_filter**: Calculate next `event.detail.target` lambda name and put event into the *eventbus*
* **async_await**: Asynchronous wait

## Note
next_filter module produce event in the following format

```
{
    "Source": "custom.myapp",
    "EventBusName": "pipe",
    "DetailType": "transaction",
    "Time": new Date(),
    "Detail":{
        "type": "filter-1-type",
        "target": "f1_lambda",
        "filterlist": [
            "f1_lambda",
            "f2_lambda",
            "f3_lambda"
        ]
    }
}
```

`next_filter` hardcoded the `EventBusName` as _pipe_. For custom eventbus, you need to change the `EventBusName` attribute.
