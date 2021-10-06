package com.myorg;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.*;
import java.util.*;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;

public class CdkForSvrlessFilters extends Stack {

    public CdkForSvrlessFilters(final Construct scope, final String id) {
        super(scope, id);

        try {

            String LayerContent = readFileAsString("../pipes-filters/layer/nodejs/utility.js");
            List runtimes = new ArrayList();
            Collections.addAll(runtimes, Runtime.NODEJS_14_X, Runtime.NODEJS_12_X);
            final LayerVersion lambdaLayer = LayerVersion.Builder.create(this, "cdk-lambda-layer")
                    .description("Dependencies to deduce next filter")
                    .layerVersionName("utility")
                    .code(Code.fromAsset("../pipes-filters/layer/"))
                    .compatibleRuntimes(runtimes)
                    .license("MIT")
                    //.removalPolicy(RemovalPolicy.RETAIN)
                    .build();

            List layers = new ArrayList();
            layers.add(lambdaLayer);

            // Get the lambda function code
            String LambdaContentFilter1 = readFileAsString("../pipes-filters/filters/filter1/index.js");
            String LambdaContentFilter2 = readFileAsString("../pipes-filters/filters/filter2/index.js");
            String LambdaContentFilter3 = readFileAsString("../pipes-filters/filters/filter3/index.js");
            //List<String> filterFolders = getFolders("../pipes-filters/filters/");

            PolicyStatement ps = PolicyStatement.Builder.create()
                    .actions(Arrays.asList("events:PutEvents"))
                    .effect(Effect.ALLOW)
                    .resources(Arrays.asList("arn:aws:events:us-east-1:645362674973:event-bus/pipe"))
                    .build();
            // Sample Lambda Function Resource
            final SingletonFunction lambdaFunctionFilter1 =
                    SingletonFunction.Builder.create(this, "cdk-lambda-filter1")
                            .functionName("filter1_lambda")
                            .description("My Custom Resource Lambda")
                            .code(Code.fromInline(LambdaContentFilter1))
                            //.code(Code.fromAsset())
                            .handler("index.handler")
                            .timeout(Duration.seconds(300))
                            .runtime(Runtime.NODEJS_14_X)
                            .layers(layers)
                            .initialPolicy(Arrays.asList(ps))
                            .uuid(UUID.randomUUID().toString())
                            .build();

            final SingletonFunction lambdaFunctionFilter2 =
                    SingletonFunction.Builder.create(this, "cdk-lambda-filter2")
                            .functionName("filter2_lambda")
                            .description("My Custom Resource Lambda")
                            .code(Code.fromInline(LambdaContentFilter2))
                            //.code(Code.fromAsset())
                            .handler("index.handler")
                            .timeout(Duration.seconds(300))
                            .runtime(Runtime.NODEJS_14_X)
                            .layers(layers)
                            .initialPolicy(Arrays.asList(ps))
                            .uuid(UUID.randomUUID().toString())
                            .build();

            final SingletonFunction lambdaFunctionFilter3 =
                    SingletonFunction.Builder.create(this, "cdk-lambda-filter3")
                            .functionName("filter3_lambda")
                            .description("My Custom Resource Lambda")
                            .code(Code.fromInline(LambdaContentFilter3))
                            //.code(Code.fromAsset())
                            .handler("index.handler")
                            .timeout(Duration.seconds(300))
                            .runtime(Runtime.NODEJS_14_X)
                            .layers(layers)
                            .initialPolicy(Arrays.asList(ps))
                            .uuid(UUID.randomUUID().toString())
                            .build();

            CfnOutput cfnoutputFilterName1 =
                    CfnOutput.Builder.create(this, "cfnoutputFilterName1")
                            .description("The message that came back from the Custom Resource")
                            .exportName("nameFilter1")
                            .value(lambdaFunctionFilter1.getFunctionName())
                            .build();
            CfnOutput cfnoutputFilterARN1 =
                    CfnOutput.Builder.create(this, "cfnoutputFilterARN1")
                            .description("The message that came back from the Custom Resource")
                            .exportName("arnFilter1")
                            .value(lambdaFunctionFilter1.getFunctionArn())
                            .build();

            CfnOutput cfnoutputFilterName2 =
                    CfnOutput.Builder.create(this, "cfnoutputFilterName2")
                            .description("The message that came back from the Custom Resource")
                            .exportName("nameFilter2")
                            .value(lambdaFunctionFilter2.getFunctionName())
                            .build();
            CfnOutput cfnoutputFilterARN2 =
                    CfnOutput.Builder.create(this, "cfnoutputFilterARN2")
                            .description("The message that came back from the Custom Resource")
                            .exportName("arnFilter2")
                            .value(lambdaFunctionFilter2.getFunctionArn())
                            .build();

            CfnOutput cfnoutputFilterName3 =
                    CfnOutput.Builder.create(this, "cfnoutputFilterName3")
                            .description("The message that came back from the Custom Resource")
                            .exportName("nameFilter3")
                            .value(lambdaFunctionFilter3.getFunctionName())
                            .build();
            CfnOutput cfnoutputFilterARN3 =
                    CfnOutput.Builder.create(this, "cfnoutputFilterARN3")
                            .description("The message that came back from the Custom Resource")
                            .exportName("arnFilter3")
                            .value(lambdaFunctionFilter3.getFunctionArn())
                            .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // function to read the file content
    public static String readFileAsString(String fileName) throws Exception {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(fileName)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<String> getFolders(String path){
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return Arrays.asList(directories);
    }
}
