package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.StackProps;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CdkForSvrlessDsgnPattApp {
    public static void main(final String args[]) {
        App app = new App();
        System.out.println(args.length);
        //System.out.println(ar);
        Properties projectProps = new Properties();
        projectProps.put("layerFolder", args[0]);
        projectProps.put("lambdaRootPath", args[1]);
        projectProps.put("filterCount", args[2]);
        projectProps.put("eventBus", args[3]);

        /**
         * pipes-filters
         */
        new CdkForSvrlessFilters(app, "cdk-filters", projectProps);
        new CdkForSvrlessPipes(app, "cdk-pipes", projectProps);
        app.synth();
    }
}
