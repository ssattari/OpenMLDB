/*
 * Copyright 2021 4Paradigm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com._4paradigm.hybridse.flink.batch.planner;

import com._4paradigm.hybridse.common.FesqlException;
import com._4paradigm.hybridse.flink.common.planner.GeneralPlanContext;
import com._4paradigm.hybridse.vm.PhysicalLimitNode;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.table.api.Table;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BatchLimitPlan {

    private static final Logger logger = LoggerFactory.getLogger(BatchLimitPlan.class);

    private static int currentCnt = 0;

    public static Table gen(GeneralPlanContext planContext, PhysicalLimitNode node, Table childTable) throws FesqlException {

        DataSet<Row> inputDataset = planContext.getBatchTableEnvironment().toDataSet(childTable, Row.class);
        final int limitCnt = node.GetLimitCnt();

        DataSet<Row> outputDataset = inputDataset.filter(new FilterFunction<Row>() {
            @Override
            public boolean filter(Row row) throws Exception {
                if (currentCnt < limitCnt) {
                    currentCnt++;
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Convert DataSet<Row> to Table
        return planContext.getBatchTableEnvironment().fromDataSet(outputDataset);
    }

}
