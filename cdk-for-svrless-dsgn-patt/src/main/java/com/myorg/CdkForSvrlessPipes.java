package com.myorg;


import software.amazon.awscdk.core.CfnParameter;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Fn;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.IRuleTarget;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Permission;
import software.amazon.awscdk.services.lambda.SingletonFunction;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;

import java.util.*;

public class CdkForSvrlessPipes extends Stack {
    public CdkForSvrlessPipes(final Construct scope, final String id, int filterCount) {
        super(scope, id);

        try{
            String eventBusName = CfnParameter.Builder.create(this, "eventBusName")
                    .type("String")
                    .description("The name of the event bus")
                    .defaultValue("pipe")
                    .build().getValueAsString();

            EventBus pipe = EventBus.Builder.create(this, eventBusName)
                    .eventBusName("pipe")
                    .build();

            for (int i=0;i<filterCount;i++){
                String nameFilter = Fn.importValue("nameFilter"+i);
                String arnFilter = Fn.importValue("arnFilter"+i);
                Map<String, List<String>> detailRule = new HashMap<>();
                detailRule.put("target", Arrays.asList(nameFilter));
                EventPattern patternRule = EventPattern.builder()
                        .detail(detailRule)
                        //.account(Arrays.asList("014725595236"))
                        //.version(Arrays.asList("0"))
                        //.detailType(Arrays.asList("filter-type"))
                        //.source(Arrays.asList("poc.dsgpatt.pf"))
                        //.region(Arrays.asList("us-east-1"))
                        .build();
                LambdaFunction filter = new LambdaFunction(Function.fromFunctionArn(this,"filter"+i,arnFilter));
                Rule rule = Rule.Builder.create(this,"rule"+i)
                        .ruleName("filter-rule-"+i)
                        .enabled(true)
                        .eventBus(pipe)
                        .eventPattern(patternRule)
                        .targets(Arrays.asList(filter))
                        .build();
            }
        }catch(Exception e){

        }

    }
}
