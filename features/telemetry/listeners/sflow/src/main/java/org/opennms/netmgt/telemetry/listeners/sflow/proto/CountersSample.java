/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.telemetry.listeners.sflow.proto;

import java.nio.ByteBuffer;
import java.util.Optional;

import org.opennms.netmgt.telemetry.listeners.api.utils.BufferUtils;
import org.opennms.netmgt.telemetry.listeners.sflow.InvalidPacketException;

import com.google.common.base.MoreObjects;

// struct counters_sample {
//   unsigned int sequence_number;   /* Incremented with each counter sample
//                                      generated by this source_id
//                                      Note: If the agent resets any of the
//                                            counters then it must also
//                                            reset the sequence_number.
//                                            In the case of ifIndex-based
//                                            source_id's the sequence
//                                            number must be reset each time
//                                            ifCounterDiscontinuityTime
//                                            changes. */
//   sflow_data_source source_id;    /* sFlowDataSource */
//   counter_record counters<>;      /* Counters polled for this source */
// }

public class CountersSample implements SampleData {

    private final long sequenceNumber;
    private final DataSource sourceId;

    private final Array<CounterRecord> counters;

    public CountersSample(final ByteBuffer buffer) throws InvalidPacketException {
        this.sequenceNumber = BufferUtils.uint32(buffer);
        this.sourceId = new DataSource(buffer);

        this.counters = new Array(buffer, Optional.empty(), CounterRecord::new);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sequenceNumber", sequenceNumber)
                .add("sourceId", sourceId)
                .add("counters", counters)
                .toString();
    }
}
