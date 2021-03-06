package de.taimos.dvalin.monitoring.aws;

/*-
 * #%L
 * Dvalin monitoring service implementation for AWS CloudWatch
 * %%
 * Copyright (C) 2016 - 2017 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import de.taimos.dvalin.cloud.aws.AWSClient;
import de.taimos.dvalin.monitoring.MetricInfo;
import de.taimos.dvalin.monitoring.MetricSender;
import de.taimos.dvalin.monitoring.MetricUnit;

/**
 * Metric sender using AWS CloudWatch
 */
@Component
public class AWSMetricSender implements MetricSender {

    @AWSClient
    private AmazonCloudWatch cloudWatch;

    @Override
    public void sendMetric(MetricInfo metric, Double value) {
        PutMetricDataRequest req = new PutMetricDataRequest();
        req.setNamespace(metric.getNamespace());

        MetricDatum metricDatum = new MetricDatum();
        metricDatum.withMetricName(metric.getMetric());
        metricDatum.withUnit(this.convertUnit(metric.getUnit()));
        metricDatum.withDimensions(this.getDimensionsFromMetricInfo(metric));
        metricDatum.withValue(value);
        req.setMetricData(Collections.singleton(metricDatum));
        this.cloudWatch.putMetricData(req);
    }

    private List<Dimension> getDimensionsFromMetricInfo(MetricInfo metric) {
        List<Dimension> dimensions = new ArrayList<>();
        for (Map.Entry<String, String> entry : metric.getDimensions().entrySet()) {
            dimensions.add(new Dimension().withName(entry.getKey()).withValue(entry.getValue()));
        }
        return dimensions;
    }

    private StandardUnit convertUnit(MetricUnit unit) {
        final StandardUnit awsunit;
        switch (unit) {
        case Bits:
            awsunit = StandardUnit.Bits;
            break;
        case BitsSecond:
            awsunit = StandardUnit.BitsSecond;
            break;
        case Bytes:
            awsunit = StandardUnit.Bytes;
            break;
        case BytesSecond:
            awsunit = StandardUnit.BytesSecond;
            break;
        case Count:
            awsunit = StandardUnit.Count;
            break;
        case CountSecond:
            awsunit = StandardUnit.CountSecond;
            break;
        case Seconds:
            awsunit = StandardUnit.Seconds;
            break;
        case Milliseconds:
            awsunit = StandardUnit.Milliseconds;
            break;
        case None:
            awsunit = StandardUnit.None;
            break;
        case Percent:
            awsunit = StandardUnit.Percent;
            break;
        default:
            throw new IllegalArgumentException("Invalid unit " + unit);
        }
        return awsunit;
    }
}
