<template>
  <div class="app-container">
    <!-- 搜索区 -->
    <div ref="filter-container" class="filter-container">
      <el-input v-model="listQuery.agentName" placeholder="终端名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-input v-model="listQuery.agentCode" placeholder="终端编号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
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
      <el-table-column label="终端名称" width="200px">
        <template slot-scope="scope">
          {{ scope.row.agentName }}
        </template>
      </el-table-column>
      <el-table-column label="终端编号" width="200px">
        <template slot-scope="scope">
          {{ scope.row.agentCode }}
        </template>
      </el-table-column>
      <el-table-column label="心跳频率(秒)" width="100px">
        <template slot-scope="scope">
          {{ scope.row.heartbeatFrequency }}
        </template>
      </el-table-column>
      <el-table-column label="在线状态" width="100px">
        <template slot-scope="scope">
          {{ scope.row.onlineStatus | _onlineStatus }}
        </template>
      </el-table-column>
      <el-table-column label="上次心跳时间" width="150px">
        <template slot-scope="scope">
          {{ scope.row.lastHeartbeatTime | _parseTime }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="150px">
        <template slot-scope="scope">
          {{ scope.row.createTime | _parseTime }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="200px">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
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
    <!-- 编辑区 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="130px" style="width: 450px; margin-left:20px;">
        <el-form-item label="终端名称" prop="agentName">
          <el-input v-model="temp.agentName"/>
        </el-form-item>
        <el-form-item label="终端编号" prop="agentCode">
          <el-input v-model="temp.agentCode"/>
        </el-form-item>
        <el-form-item label="心跳频率" prop="heartbeatFrequency">
          <el-input-number v-model="temp.heartbeatFrequency" :min="1"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="editAgent">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getUserAgents, editAgent, deleteByAgentId } from '@/api/agent'
import { parseTime } from '@/util'
export default {
  filters: {
    _onlineStatus(status) {
      const map = {
        0: '在线',
        1: '当天离线',
        2: '离线 2 天',
        3: '离线 3 天以上'
      }
      return map[status]
    },
    _parseTime(time) {
      const option = '{y}-{m}-{d} {h}:{i}:{s}'
      return parseTime(time, option)
    }
  },
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
      tableHeight: '80vh',
      // 编辑区
      dialogFormVisible: false,
      dialogTitle: '',
      rules: {
        agentName: [{ required: true, trigger: 'blur', message: 'Agent Name is required' }],
        agentCode: [{ required: true, trigger: 'blur', message: 'Agent Code is required' }],
        heartbeatFrequency: [{ required: true, trigger: 'blur', message: 'Heartbeat Frequency is required' }]
      }
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
      getUserAgents(this.listQuery).then(res => {
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
      this.temp = {
        heartbeatFrequency: 60
      }
      this.dialogTitle = '添加'
      this.dialogFormVisible = true
    },
    // 修改
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogTitle = '修改'
      this.dialogFormVisible = true
    },
    // 提交
    editAgent() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          editAgent(this.temp).then((res) => {
            if (res.code === 0) {
              // 重新请求一次数据
              this.handleFilter()
              this.dialogFormVisible = false
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$msgbox({
        title: '确认删除？',
        message: '删除之后无法恢复哦...',
        showCancelButton: true,
        confirmButtonText: '确认',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 删除
            deleteByAgentId(row.agentId).then(res => {
              if (res.code === 0) {
                this.$message.success(res.msg)
                // 更新
                this.handleFilter()
              } else {
                this.$message.error(res.msg)
              }
            })
          } else {
            this.$message.info('取消')
          }
          // 关闭弹窗
          done()
        }
      })
    }
  }
}
</script>