package com.myorg;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.*;
import java.util.*;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.codebuild.Project;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;

public class CdkForSvrlessFilters extends Stack {

    public CdkForSvrlessFilters(final Construct scope, final String id, final Properties projectProps) {
        super(scope, id);
        try {

            /*String eventBusName = CfnParameter.Builder.create(this, "eventBusName")
                    .type("String")
                    .description("The name of the event bus")
                    .defaultValue("pipe")
                    .build().getValueAsString();*/

            String layerName = CfnParameter.Builder.create(this, "layerName")
                    .type("String")
                    .description("The name of the lambda layer")
                    .defaultValue("utility")
                    .build().getValueAsString();

            List<String> lambdaNames = CfnParameter.Builder.create(this, "lambdaNames")
                    .type("CommaDelimitedList")
                    .description("The name of the lambdas")
                    .defaultValue("f1_lambda,f2_lambda,f3_lambda")
                    .build().getValueAsList();
            System.out.println(lambdaNames.size());

            String layerFolder = (String) projectProps.get("layerFolder");
            String lambdaRootPath = (String) projectProps.get("lambdaRootPath");
            String eventBusName = (String) projectProps.get("eventBus");
            List<Runtime> runtimes = new ArrayList();
            Collections.addAll(runtimes, Runtime.NODEJS_14_X, Runtime.NODEJS_12_X);

            final LayerVersion lambdaLayer = LayerVersion.Builder.create(this, "cdk-lambda-layer")
                    .description("Dependencies to deduce next filter")
                    .layerVersionName(layerName)
                    .code(Code.fromAsset(layerFolder))
                    .compatibleRuntimes(runtimes)
                    .license("MIT")
                    //.removalPolicy(RemovalPolicy.RETAIN)
                    .build();

            List layers = new ArrayList();
            layers.add(lambdaLayer);

            PolicyStatement ps = PolicyStatement.Builder.create()
                    .actions(Arrays.asList("events:PutEvents"))
                    .effect(Effect.ALLOW)
                    .resources(Arrays.asList("arn:aws:events:"+this.getRegion()+":"+this.getAccount()+":event-bus/"+eventBusName))
                    .build();
            List<String> filterFolders = getFolders(projectProps.getProperty("lambdaRootPath"));
            if (filterFolders.size()>0){
                int i = 0;
                for(String ff: filterFolders){
                    String filePath = projectProps.getProperty("lambdaRootPath") + ff + "/index.js";
                    // Get the lambda function code
                    String LambdaContentFilter = readFileAsString(filePath);
                    String lambdaName = Fn.select(i,lambdaNames);

                    final SingletonFunction lambdaFunctionFilter =
                            SingletonFunction.Builder.create(this, "cdk-lambda-filter"+i)
                                    .functionName(lambdaName)
                                    .description("My Custom Resource Lambda")
                                    .code(Code.fromInline(LambdaContentFilter))
                                    //.code(Code.fromAsset())
                                    .handler("index.handler")
                                    .timeout(Duration.seconds(300))
                                    .runtime(Runtime.NODEJS_14_X)
                                    .layers(layers)
                                    .initialPolicy(Arrays.asList(ps))
                                    .uuid(UUID.randomUUID().toString())
                                    .build();

                    CfnOutput cfnoutputFilterName =
                            CfnOutput.Builder.create(this, "cfnoutputFilterName"+i)
                                    .description("The message that came back from the Custom Resource")
                                    .exportName("nameFilter"+i)
                                    .value(lambdaFunctionFilter.getFunctionName())
                                    .build();
                    CfnOutput cfnoutputFilterARN =
                            CfnOutput.Builder.create(this, "cfnoutputFilterARN"+i)
                                    .description("The message that came back from the Custom Resource")
                                    .exportName("arnFilter"+i)
                                    .value(lambdaFunctionFilter.getFunctionArn())
                                    .build();

                    i+=1;
                }
            }
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
