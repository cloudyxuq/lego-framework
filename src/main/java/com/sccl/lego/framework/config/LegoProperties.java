package com.sccl.lego.framework.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基础配置
 * 配置数据配置在application.yml文件中
 *
 * @author xuq
 */
@Component
@ConfigurationProperties(prefix = "lego", ignoreInvalidFields = false)
public class LegoProperties {
    /**
     * swagger api文档对象（自定义）
     */
    private final Swagger swagger = new Swagger();
    /**
     * 监控（包含系统监控和业务监控）
     */
    private final Metrics metrics = new Metrics();
    /**
     * 便民中心注册中心配置（安全）
     */
    private final Registry registry = new Registry();

    /**
     * 日志记录（包含度量指标）
     */
    private final Logging logging = new Logging();



    public Swagger getSwagger() {
        return swagger;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public Registry getRegistry() {
        return registry;
    }

    public Logging getLogging() {
        return logging;
    }


    /**
     * 自定义Swagger 接口文档内部类
     */
    public static class Swagger {

        private String title = "LEGO API";

        private String description = "LEGO服务文档";

        private String version = "0.0.1";

        private String termsOfServiceUrl;

        private String contactName;

        private String contactUrl;

        private String contactEmail;

        private String license;

        private String licenseUrl;

        private String defaultIncludePattern = "/api/.*";

        private String host;

        private String basePackage="com.sccl.lego";

        private String[] protocols = {};

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getDefaultIncludePattern() {
            return defaultIncludePattern;
        }

        public void setDefaultIncludePattern(String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String[] getProtocols() {
            return protocols;
        }

        public void setProtocols(String[] protocols) {
            this.protocols = protocols;
        }

        public String getBasePackage() {
            return basePackage;
        }

        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }
    }

    /**
     * 自定义指标库工具类
     */
    public static class Metrics {
        private final Jmx jmx = new Jmx();

        private final Graphite graphite = new Graphite();

        private final Prometheus prometheus = new Prometheus();

        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return jmx;
        }

        public Graphite getGraphite() {
            return graphite;
        }

        public Prometheus getPrometheus() {
            return prometheus;
        }

        public Logs getLogs() {
            return logs;
        }

        /**
         * 支持java管理扩展
         */
        public static class Jmx {
            private boolean enabled = true;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        /**
         * 自定义Graphite系统监控属性
         */
        public static class Graphite {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 2003;

            private String prefix = "cscApplication";

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getPrefix() {
                return prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }
        }

        /**
         * 自定义普罗米修斯业务监控
         */
        public static class Prometheus {

            /**
             * 是否启用
             */
            private boolean enabled = false;
            /**
             * 请求路径
             */
            private String endpoint = "/prometheusMetrics";

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getEndpoint() {
                return endpoint;
            }

            public void setEndpoint(String endpoint) {
                this.endpoint = endpoint;
            }
        }

        public static class Logs {

            private boolean enabled = false;

            private long reportFrequency = 60;

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(int reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    /**
     * 便民服务注册中心安全（账户密码）
     */
    private static class Registry {

        /**
         * 注册中心用户名
         */
        private String name;
        /**
         * 注册中心密码
         */
        private String pwd;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }

    /**
     * 自定义日志（采用logstash）
     */
    public static class Logging {

        /**
         * logstash日志记录
         */
        private final Logstash logstash = new Logstash();
        /**
         * spring cloud 度量指标
         */
        private final SpectatorMetrics spectatorMetrics = new SpectatorMetrics();

        public Logstash getLogstash() {
            return logstash;
        }

        public SpectatorMetrics getSpectatorMetrics() {
            return spectatorMetrics;
        }

        /**
         * 自定义logstash配置
         */
        public static class Logstash {

            /**
             * 是否启用logstash，默认fasle
             */
            private boolean enabled = false;
            /**
             * host，默认为localhost
             */
            private String host = "localhost";
            /**
             * logstash端口，默认5000
             */
            private int port = 5000;

            /**
             * logstash队列大小，默认512
             */
            private int queueSize = 512;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }

        /**
         * spring cloud Sepectator度量指标
         */
        public static class SpectatorMetrics {

            private boolean enabled = false;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }


}
