<template>
  <div>
    <!-- 搜索区 -->
    <div ref="filter-container" class="filter-container">
      <el-input v-model="listQuery.oldName" placeholder="文件名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.fileType" placeholder="文件类型" clearable style="width: 200px;" class="filter-item" @change="handleFilter">
        <el-option label="图片" value="1"/>
        <el-option label="视频" value="2"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-upload" @click="upload">上传</el-button>
      <el-button class="filter-item" type="success" icon="el-icon-refresh" @click="refresh">刷新</el-button>
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
      highlight-current-row
    >
      <el-table-column label="序号" width="60px" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="文件名称" width="250px">
        <template slot-scope="scope">
          <el-tooltip :content="scope.row.oldName" effect="dark" placement="top-start">
            <span class="one-line">{{ scope.row.oldName }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="文件类型" width="100px">
        <template slot-scope="scope">
          <span>{{ scope.row.fileType | _fileType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件大小(kb)" width="120px">
        <template slot-scope="scope">
          <span>{{ scope.row.fileSize / 1000 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="150px" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | _parseTime() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="auto">
        <template slot-scope="scope">
          <el-button type="success" size="mini" @click="handlePreview(scope.row.filePath)">预览</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">重命名</el-button>
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
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 上传文件 -->
    <el-dialog :visible.sync="dialogFilesVisible" :before-close="closeDialog" title="上传文件" :close-on-click-modal="false" width="500px">
      <el-form class="small-space" label-position="right" label-width="130px">
        <el-form-item label="存储目录">
          <el-input v-model="directoryName" disabled/>
        </el-form-item>
        <!-- vue-simple-upload 可以分片上传 -->
        <simple-uploader v-if="dialogFilesVisible" :file-directory-id="fileDirectoryId"/>
      </el-form>
    </el-dialog>
    <!-- 编辑区 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="文件名称" prop="oldName">
          <el-input v-model="temp.oldName"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
const CODE_OK = 0
import SimpleUploader from './simple-uploader'
import { getFiles, editFile, deleteFile } from '@/api/file'
import { parseTime } from '@/util'
export default {
  name: 'FileTable',
  components: {
    SimpleUploader
  },
  filters: {
    _fileType(fileType) {
      const fileTypeMap = {
        1: '图片',
        2: '视频'
      }
      return fileTypeMap[fileType]
    },
    _parseTime(time) {
      const option = '{y}-{m}-{d} {h}:{i}:{s}'
      return parseTime(time, option)
    }
  },
  props: {
    directoryId: {
      type: String,
      default: null
    },
    directoryName: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      // 查询属性
      listQuery: {
        pageNo: 1,
        pageSize: 20
      },
      totalCount: 0,
      tableHeight: '70vh',
      // 编辑区属性
      dialogFormVisible: false,
      dialogPayFormVisible: false,
      dialogTitle: '',
      temp: {},
      // 文件上传相关属性
      dialogFilesVisible: false,
      fileDirectoryId: null,
      // 校验规则
      rules: {
        oldName: [{ required: true, message: 'file name is required', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.calcTableHeight()
    this.loadData()
  },
  methods: {
    // 表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        this.tableHeight = window.innerHeight - this.$refs['filter-container'].clientHeight - this.$refs['pagination-container'].clientHeight - 170 + 'px'
      })
    },
    // 加载数据
    loadData() {
      this.listLoading = true
      this.listQuery.directoryId = this.directoryId
      getFiles(this.listQuery).then(res => {
        // 加载数据成功
        if (res.code === CODE_OK) {
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
    // 刷新
    refresh() {
      this.directoryId = null
      this.handleFilter()
    },
    // 查询
    handleFilter() {
      this.listQuery.pageNo = 1
      this.loadData()
    },
    // 删除文件
    handleDelete(row) {
      this.$msgbox({
        title: '确认删除？',
        message: '删除之后无法恢复哦。。。',
        showCancelButton: true,
        confirmButtonText: '确认',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 删除企业接口
            deleteFile(row.fileId).then(res => {
              if (res.code === CODE_OK) {
                this.$message.success(res.msg)
                // 刷新
                this.handleFilter()
              } else {
                this.$message.error(res.msg)
              }
            })
          } else {
            this.$message.info('cancel')
          }
          // 关闭弹窗
          done()
        }
      })
    },
    // 文件上传
    upload() {
      // 检测directoryId 是否为空
      if (this.directoryId === null) {
        this.$message({
          type: 'error',
          message: '请选择上传的目录'
        })
      } else {
        this.$nextTick(() => {
          // 给选定的目录赋值
          this.fileDirectoryId = this.directoryId
          this.dialogFilesVisible = true
        })
      }
    },
    handlePreview(path) {
      window.open(path)
    },
    // 加载对话框 update
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogTitle = '修改'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // update action
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          editFile(this.temp).then((res) => {
            if (res.code === CODE_OK) {
              // 刷新
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
    // 关闭弹窗
    closeDialog(done) {
      done()
      this.handleFilter()
    }
  }
}
</script>
<style lang="scss" scoped>

</style>

