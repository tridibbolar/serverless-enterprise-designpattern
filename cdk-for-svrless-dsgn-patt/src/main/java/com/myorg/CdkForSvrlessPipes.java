package com.myorg;


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
    public CdkForSvrlessPipes(final Construct scope, final String id) {
        super(scope, id);

        try{
            EventBus pipe = EventBus.Builder.create(this, "pipe")
                    .eventBusName("pipe")
                    .build();

            String nameFilter1 = Fn.importValue("nameFilter1");
            String nameFilter2 = Fn.importValue("nameFilter2");
            String nameFilter3 = Fn.importValue("nameFilter3");
            String arnFilter1 = Fn.importValue("arnFilter1");
            String arnFilter2 = Fn.importValue("arnFilter2");
            String arnFilter3 = Fn.importValue("arnFilter3");


            Map<String, List<String>> detailRule1 = new HashMap<>();
            detailRule1.put("target", Arrays.asList(nameFilter1));
            Map<String, List<String>> detailRule2 = new HashMap<>();
            detailRule2.put("target", Arrays.asList(nameFilter2));
            Map<String, List<String>> detailRule3 = new HashMap<>();
            detailRule3.put("target", Arrays.asList(nameFilter3));
            //detail.put("type", Arrays.asList("filter-1-type"));
            //detail.put("filterlist", Arrays.asList(nameFilter1, nameFilter2, nameFilter3));

            EventPattern patternRule1 = EventPattern.builder()
                    .detail(detailRule1)
                    //.account(Arrays.asList("014725595236"))
                    //.version(Arrays.asList("0"))
                    //.detailType(Arrays.asList("filter-type"))
                    //.source(Arrays.asList("poc.dsgpatt.pf"))
                    //.region(Arrays.asList("us-east-1"))
                    .build();
            EventPattern patternRule2 = EventPattern.builder()
                    .detail(detailRule2)
                    .build();
            EventPattern patternRule3 = EventPattern.builder()
                    .detail(detailRule3)
                    .build();


            LambdaFunction filter1 = new LambdaFunction(Function.fromFunctionArn(this,"filter1",arnFilter1));
            LambdaFunction filter2 = new LambdaFunction(Function.fromFunctionArn(this,"filter2",arnFilter2));
            LambdaFunction filter3 = new LambdaFunction(Function.fromFunctionArn(this,"filter3",arnFilter3));
            //IRuleTarget filter1 =
            Rule rule1 = Rule.Builder.create(this,"rule1")
                    .ruleName("filter-rule-1")
                    .enabled(true)
                    .eventBus(pipe)
                    .eventPattern(patternRule1)
                    .targets(Arrays.asList(filter1))
                    .build();

            Rule rule2 = Rule.Builder.create(this,"rule2")
                    .ruleName("filter-rule-2")
                    .enabled(true)
                    .eventBus(pipe)
                    .eventPattern(patternRule2)
                    .targets(Arrays.asList(filter2))
                    .build();

            Rule rule3 = Rule.Builder.create(this,"rule3")
                    .ruleName("filter-rule-3")
                    .enabled(true)
                    .eventBus(pipe)
                    .eventPattern(patternRule3)
                    .targets(Arrays.asList(filter3))
                    .build();

        }catch(Exception e){

        }

    }
}
