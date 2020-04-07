<template>
  <div class="app-container">
    <!-- 搜索区 -->
    <div ref="filter-container" class="filter-container">
      <el-input v-model="listQuery.userName" placeholder="User Name" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>
    <!-- 表格区 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :height="tableHeight"
      element-loading-text="Loading"
      size="mini"
      border
      stripe
      highlight-current-row>
      <el-table-column label="序号" width="60px" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页区 -->
    <div ref="pagination-container" class="pagination-container">
      <el-pagination
        :current-page="listQuery.pageNo"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="listQuery.pageSize"
        :total="totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </div>
  </div>
</template>
<script>
import { getUsers } from '@/api/user'
export default {
  data() {
    return {
      listQuery: {
        pageNo: 1,
        pageSize: 20
      },
      list: [],
      totalCount: 0,
      listLoading: true,
      temp: {},
      tableHeight: '80vh'
    }
  },
  created() {
    this.calcTableHeight()
    this.handleFilter()
  },
  methods: {
    // 表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        this.tableHeight = window.innerHeight - this.$refs['filter-container'].clientHeight - this.$refs['pagination-container'].clientHeight - 120 + 'px'
      })
    },
    // 加载数据
    loadData() {
      this.listLoading = true
      getUsers(this.listQuery).then(res => {
        // 加载数据成功
        if (res.code === 0) {
          this.totalCount = res.totalCount
          this.list = res.data
          this.listLoading = false
        } else {
          this.$notify.error({
            title: '失败',
            message: res.msg
          })
        }
      })
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNo = val
      this.loadData()
    },
    // 查询
    handleFilter() {
      this.listQuery.pageNo = 1
      this.loadData()
    },
    // 添加
    handleCreate() {

    }
  }
}
</script>