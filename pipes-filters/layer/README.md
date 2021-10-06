# To create layer

### To build layer zip file execute below command inside from ```layer``` folder
```zip -r nextfilter.zip ./*```

{
    "version": "0",
    "id": "2f2445f1-7c99-7b3b-db09-001f8816ff4a",
    "detail-type": "filter-type",
    "source": "poc.dsgpatt.pf",
    "account": "645362674973",
    "time": "2021-10-04T07:08:16Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "type": "filter-1-type",
        "target": "filter1_lambda",
        "filterlist": [
            "filter1_lambda",
            "filter2_lambda",
            "filter3_lambda"
        ]
    }
}
