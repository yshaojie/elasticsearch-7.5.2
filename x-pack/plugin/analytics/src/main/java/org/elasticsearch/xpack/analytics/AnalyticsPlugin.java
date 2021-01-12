/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.analytics;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.license.XPackLicenseState;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.xpack.core.XPackPlugin;
import org.elasticsearch.xpack.core.analytics.action.AnalyticsStatsAction;
import org.elasticsearch.xpack.analytics.action.TransportAnalyticsStatsAction;
import org.elasticsearch.xpack.analytics.cumulativecardinality.CumulativeCardinalityPipelineAggregationBuilder;
import org.elasticsearch.xpack.analytics.cumulativecardinality.CumulativeCardinalityPipelineAggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.singletonList;

public class AnalyticsPlugin extends Plugin implements SearchPlugin, ActionPlugin {

    // TODO this should probably become more structured once Analytics plugin has more than just one agg
    public static AtomicLong cumulativeCardUsage = new AtomicLong(0);
    private final boolean transportClientMode;

    public AnalyticsPlugin(Settings settings) {
        this.transportClientMode = XPackPlugin.transportClientMode(settings);
    }

    public static XPackLicenseState getLicenseState() { return XPackPlugin.getSharedLicenseState(); }

    @Override
    public List<PipelineAggregationSpec> getPipelineAggregations() {
        return singletonList(new PipelineAggregationSpec(
            CumulativeCardinalityPipelineAggregationBuilder.NAME,
            CumulativeCardinalityPipelineAggregationBuilder::new,
            CumulativeCardinalityPipelineAggregator::new,
            CumulativeCardinalityPipelineAggregationBuilder::parse));
    }

    @Override
    public List<ActionPlugin.ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        return singletonList(
            new ActionHandler<>(AnalyticsStatsAction.INSTANCE, TransportAnalyticsStatsAction.class));
    }

    @Override
    public Collection<Module> createGuiceModules() {
        List<Module> modules = new ArrayList<>();

        if (transportClientMode) {
            return modules;
        }

        modules.add(b -> XPackPlugin.bindFeatureSet(b, AnalyticsFeatureSet.class));
        return modules;
    }
}
