package com.sccl.lego.framework.config.metrics;

import com.sccl.lego.framework.config.LegoProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *Graphite系统监控
 */
//@Configuration
@ConditionalOnClass(Graphite.class)
public class GraphiteRegistry {
    private final Logger log = LoggerFactory.getLogger(GraphiteRegistry.class);

    private final LegoProperties legoProperties;

    public GraphiteRegistry(MetricRegistry metricRegistry, LegoProperties legoProperties) {
        this.legoProperties = legoProperties;
        //启用Graphite系统监控
        if (this.legoProperties.getMetrics().getGraphite().isEnabled()) {
            log.info("初始化Graphite系统监控");
            String graphiteHost = legoProperties.getMetrics().getGraphite().getHost();
            Integer graphitePort = legoProperties.getMetrics().getGraphite().getPort();
            String graphitePrefix = legoProperties.getMetrics().getGraphite().getPrefix();
            Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
            GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .prefixedWith(graphitePrefix)
                .build(graphite);
            graphiteReporter.start(1, TimeUnit.MINUTES);
        }
    }
}
