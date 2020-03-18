package com.sccl.lego.framework.config.metrics;

import com.sccl.lego.framework.config.LegoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 普罗米修斯监控（提供一系列指标度量,业务监控）
 *
 * @author xuq
 */
//@Configuration
@ConditionalOnClass(CollectorRegistry.class)
public class PrometheusRegistry implements ServletContextInitializer {

    private final Logger log = LoggerFactory.getLogger(PrometheusRegistry.class);

    /**
     * 指标度量（dropwized）
     */
    private final MetricRegistry metricRegistry;

    private final LegoProperties legoProperties;

    public PrometheusRegistry(MetricRegistry metricRegistry, LegoProperties legoProperties) {
        this.metricRegistry = metricRegistry;
        this.legoProperties = legoProperties;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //启用普罗米修斯业务监控
        if (legoProperties.getMetrics().getPrometheus().isEnabled()) {
            //普罗米修斯endpoint（请求路径）
            String point = legoProperties.getMetrics().getPrometheus().getEndpoint();
            log.info("初始化Prometheus监控，请求路径：{}", point);
            CollectorRegistry collectorRegistry = new CollectorRegistry();
            //注册采集器
            collectorRegistry.register(new DropwizardExports(metricRegistry));
            servletContext.addServlet("prometheusMetrics", new io.prometheus.client.exporter.MetricsServlet(collectorRegistry)).addMapping(point);
        }
    }
}
