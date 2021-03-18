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

import com._4paradigm.hybridse.flink.common.planner.GeneralPlanContext;
import com._4paradigm.hybridse.vm.PhysicalDataProviderNode;
import org.apache.flink.table.api.Table;

public class BatchDataProviderPlan {

    public static Table gen(GeneralPlanContext planContext, PhysicalDataProviderNode dataProviderNode) {
        return planContext.getBatchTableEnvironment().scan(dataProviderNode.GetName());
    }

}
