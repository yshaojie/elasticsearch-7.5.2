/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.analytics;

import org.elasticsearch.xpack.analytics.cumulativecardinality.CumulativeCardinalityPipelineAggregationBuilder;

public class AnalyticsAggregationBuilders {

    public static CumulativeCardinalityPipelineAggregationBuilder cumulativeCaardinality(String name, String bucketsPath) {
        return new CumulativeCardinalityPipelineAggregationBuilder(name, bucketsPath);
    }
}
