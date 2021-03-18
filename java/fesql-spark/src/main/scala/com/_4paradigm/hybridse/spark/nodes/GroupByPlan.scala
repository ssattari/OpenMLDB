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

package com._4paradigm.hybridse.spark.nodes

import com._4paradigm.hybridse.spark.utils.SparkColumnUtil
import com._4paradigm.hybridse.spark.{FeSQLConfig, PlanContext, SparkInstance}
import com._4paradigm.hybridse.vm.PhysicalGroupNode
import org.apache.spark.sql.Column

import scala.collection.mutable

object GroupByPlan {

  def gen(ctx: PlanContext, node: PhysicalGroupNode, input: SparkInstance): SparkInstance = {
    val inputDf = input.getDfConsideringIndex(ctx, node.GetNodeId())

    val groupByExprs = node.group().keys()
    val groupByCols = mutable.ArrayBuffer[Column]()
    for (i <- 0 until groupByExprs.GetChildNum()) {
      val expr = groupByExprs.GetChild(i)

      val colIdx = SparkColumnUtil.resolveColumnIndex(expr, node.GetProducer(0))
      groupByCols += SparkColumnUtil.getColumnFromIndex(inputDf, colIdx)
    }

    val partitions = ctx.getConf.groupPartitions
    val groupedDf = if (partitions > 0) {
      inputDf.repartition(partitions, groupByCols: _*)
    } else {
      inputDf.repartition(groupByCols: _*)
    }

    SparkInstance.createConsideringIndex(ctx, node.GetNodeId(), groupedDf)
  }

}
