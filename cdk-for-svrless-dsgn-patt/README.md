# Welcome to Serverless Design Pattern Java CDK project!

## To execute this project

* `cdk ls`: To list all stacks
* `cdk deploy`: To deploy CloudFormation stacks from `cdk.out` folder into configured AWS environment

## Important files 

* `cdk.json`: This files contains maven compile and exec command to execute the main class. 3 programme arguments are mandatory.
  1. Relative path for lambda layer folder (*Eg: ../pipes-filters/layer/*)
  2. Relative path for filters' root folder (*Eg: ../pipes-filters/filters/*)
  3. Total filter count(*Eg: 3*)
* `CdkForSvrlessDsgnPattApp`: Main class. This class instantiate other stacks.
* `CdkForSvrlessFilters`: Stack class to create filter lambdas. Default eventbusname is *pipe*, lambda layer name is *utility* and lambda names are f1_lambda,f2_lambda and f3_lambda lambda. User can provide custom value as a cdk command line parameter for these components.
* `CdkForSvrlessPipes`: Stack class to create Eventbus and Rules.

Enjoy!
