<template>
  <div class="app-container">
    <!-- 搜索区 -->
    <div ref="filter-container" class="filter-container">
      <el-input v-model="listQuery.title" placeholder="Title" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.editStatus" placeholder="Edit Status" filterable clearable class="filter-item" @change="handleFilter">
        <el-option label="未完成" value="0"/>
        <el-option label="已完成" value="1"/>
        <el-option label="已撤销" value="2"/>
      </el-select>
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
      <el-table-column label="标题" width="200px">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column label="封面图" width="100px" header-align="left" align="center">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="hover">
            <img :src="scope.row.coverImage" style="max-width: 500px">
            <img slot="reference" :src="scope.row.coverImage" alt="封面图" width="25px">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="编辑状态" width="100px">
        <template slot-scope="scope">
          {{ scope.row.editStatus | _editStatus }}
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="820px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="130px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <wang-editor :content="temp.content" @editorContent="getEditorContent" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="editArticle">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import WangEditor from '@/components/wang-editor'
import { getArticles } from '@/api/article'
import { parseTime } from '@/util'
export default {
  components: {
    WangEditor
  },
  filters: {
    _editStatus(status) {
      const map = {
        0: '未完成',
        1: '已完成',
        2: '已撤销'
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
      rules: {}
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
      getArticles(this.listQuery).then(res => {
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
      this.dialogTitle = '添加'
      this.dialogFormVisible = true
    },
    // 修改
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogTitle = '修改'
      this.dialogFormVisible = true
    },
    // 编辑
    editArticle() {

    },
    // 删除
    handleDelete(row) {
      console.log(row)
    },
    // 获取编辑器内容
    getEditorContent(data) {
      this.temp.content = data
    }
  }
}
</script>