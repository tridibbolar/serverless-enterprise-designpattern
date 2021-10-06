package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.StackProps;

import java.util.Arrays;

public class CdkForSvrlessDsgnPattApp {
    public static void main(final String args[]) {
        App app = new App();

        /**
         * pipes-filters
         */
        new CdkForSvrlessFilters(app, "cdk-filters");
        new CdkForSvrlessPipes(app, "cdk-pipes");
        app.synth();
    }
}
