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
      <el-table-column label="标题" width="250px">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column label="封面图" width="100px" header-align="left" align="center">
        <template slot-scope="scope">
          <el-popover
            v-show="scope.row.coverImage"
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
      <el-table-column label="操作" width="auto">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handlePreview(scope.row)">预览</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button :disabled="scope.row.editStatus === 2" type="primary" size="mini" @click="handleCancel(scope.row)">撤销</el-button>
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
    <!-- 预览 -->
    <el-dialog :title="dialogTitle" :visible.sync="previewVisible" width="820px">
      <div style="margin: 0 auto">
        <h2 style="text-align: center">{{ temp.title }}</h2>
        <img :src="temp.coverImage" class="cover-image">
        <div v-html="temp.content"></div>
      </div>
    </el-dialog>
    <!-- 编辑区 -->
    <el-drawer :visible.sync="dialogFormVisible" size="820px" class="drawer">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="130px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title"/>
        </el-form-item>
        <el-form-item label="编辑状态" prop="editStatus">
          <el-select v-model="temp.editStatus" filterable class="filter-item">
            <el-option label="未完成" :value="0"/>
            <el-option label="已完成" :value="1"/>
            <el-option label="已撤销" :value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item label="封面" prop="coverImage">
          <div class="cover-div">
            <el-button class="item" type="primary" size="mini" @click="chooseCoverImage">选择封面图片</el-button>
            <el-switch class="item" v-model="coverImageVisible" inactive-text="隐藏" active-text="显示" />
          </div>
          <el-image
            v-show="coverImageVisible"
            :src="temp.coverImage"
            :preview-src-list="srcList"
            style="width: 300px; height: 200px;"/>
        </el-form-item>
        <el-form-item label="素材库">
          <el-button class="item" type="primary" size="mini" @click="chooseLibrary">选择素材</el-button>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <wang-editor ref="wang-editor" :content="temp.content" @editorContent="getEditorContent" />
        </el-form-item>
        <el-form-item>
          <div class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="editArticle">确认</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-drawer>
    <!-- 素材 -->
    <el-dialog :title="dialogTitle" :visible.sync="libraryVisible" width="820px">
      <file-library v-if="libraryVisible" @chooseFile="chooseFile"/>
    </el-dialog>
  </div>
</template>
<script>
import WangEditor from '@/components/wang-editor'
import FileLibrary from '@/components/file-library'
import { getArticles, editArticle, deleteByArticleId } from '@/api/article'
import { parseTime } from '@/util'
export default {
  components: {
    WangEditor,
    FileLibrary
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
      coverImageVisible: true,
      libraryVisible: false,
      previewVisible: false,
      rules: {
        title: [{ required: true, trigger: 'blur', message: 'Title is required' }],
        editStatus: [{ required: true, trigger: 'blur', message: 'Edit Status is required' }],
        content: [{ required: true, trigger: 'blur', message: 'Content is required' }],
      }
    }
  },
  computed: {
    srcList() {
      return [this.temp.coverImage]
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
      this.temp = {}
      this.dialogTitle = '添加'
      this.temp.editStatus = 0
      this.dialogFormVisible = true
    },
    // 预览
    handlePreview(row) {
      this.temp = Object.assign({}, row)
      this.dialogTitle = '预览'
      this.previewVisible = true
    },
    // 修改
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogTitle = '修改'
      this.dialogFormVisible = true
    },
    // 编辑
    editArticle() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          editArticle(this.temp).then((res) => {
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
    // 撤销
    handleCancel(row) {
      this.$msgbox({
        title: '确认撤销？',
        message: '撤销之后无法发布...',
        showCancelButton: true,
        confirmButtonText: '确认',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 撤销
            const data = {
              articleId: row.articleId,
              editStatus: 2
            }
            editArticle(data).then(res => {
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
            deleteByArticleId(row.agentId).then(res => {
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
    },
    // 获取编辑器内容
    getEditorContent(data) {
      this.temp.content = data
    },
    // 选择封面图
    chooseCoverImage() {
      this.dialogTitle = '选择封面图'
      this.libraryVisible = true
    },
    // 选择素材库
    chooseLibrary() {
      this.dialogTitle = '选择素材库'
      this.libraryVisible = true
    },
    // 获取选择的文件
    chooseFile(file) {
      this.libraryVisible = false
      if (this.dialogTitle === '选择封面图') {
        if (file.fileType === 2) {
          this.$message.error('请重新选择图片')
          return
        }
        this.temp.coverImage = file.filePath
      } else {
        let html = ''
        if (file.fileType === 1) {
          // 插入图片
          html = `<img src="${file.filePath}">`
        } else if (file.fileType === 2) {
          // 插入视频
          html = `<video src="${file.filePath}" controls></video>`
        } else {
          this.$message.error('不支持的文件类型')
          return
        }
        this.$refs['wang-editor'].editor.cmd.do('insertHTML', html)
      }
    }
  }
}
</script>
<style>
.el-drawer__body {
  overflow-y: scroll;
}
</style>
<style lang="scss" scoped>
.drawer {
  .el-form {
    padding-right: 15px;
  }
  .cover-div {
    display: block;
    .item {
      margin-right: 10px;
    }
  }
}
.cover-image {
  display: block;
  max-width: 90%;
  margin: 0 auto;
}
</style>