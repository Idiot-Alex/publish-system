<template>
  <div style="height: 76vh">
    <div class="dir-container">
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="append">添加根目录</el-button>
    </div>
    <el-tree
      :data="directoryList"
      :props="treeProps"
      :load="loadNode"
      :expand-on-click-node="false"
      lazy
      highlight-current
      default-expand-all
      node-key="directoryId"
      @node-click="nodeClick">
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>{{ node.label }}</span>
        <span>
          <el-tooltip content="添加子目录" placement="top-start">
            <el-button type="text" size="mini" icon="el-icon-plus" @click="() => append(data)"/>
          </el-tooltip>
          <el-tooltip content="重命名" placement="top-start">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="() => rename(data)"/>
          </el-tooltip>
          <el-tooltip content="删除" placement="top-start">
            <el-button type="text" size="mini" icon="el-icon-delete" @click="() => remove(node, data)"/>
          </el-tooltip>
        </span>
      </span>
    </el-tree>
    <!-- 编辑目录区 -->
    <el-dialog :title="dialogTitle" :visible.sync="directoryDialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="130px" style="width: 400px; margin-left:20px;">
        <el-form-item label="目录名称" prop="directoryName">
          <el-input v-model="temp.directoryName"/>
        </el-form-item>
        <el-form-item v-if="temp.parentDirectoryId" label="父级目录">
          <el-input v-model="temp.parentDirectoryName" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="directoryDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="editDirectory">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
const CODE_OK = 0
import { getRootDirectories, getChildDirectoies, editDirectory, deleteByDirectoryId } from '@/api/directory'
export default {
  data() {
    return {
      // 目录树属性
      directoryList: [],
      treeProps: {
        children: 'children',
        label: 'directoryName'
      },
      // 目录编辑属性
      dialogTitle: '',
      directoryDialogFormVisible: false,
      temp: {},
      // 校验规则
      rules: {
        directoryName: [{ required: true, message: 'directoryName is required', trigger: 'blur' }]
      }
    }
  },
  computed: {
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    loadData() {
      getRootDirectories().then(res => {
        if (CODE_OK === res.code) {
          this.directoryList = res.data
        }
      })
    },
    // 点击菜单  加载子菜单  懒加载
    loadNode(node, resolve) {
      const data = node.data
      if (data.directoryId) {
        setTimeout(() => {
          getChildDirectoies(data.directoryId).then(res => {
            if (CODE_OK === res.code) {
              resolve(res.data)
            }
          })
        }, 500)
      }
    },
    // 添加子目录
    append(data) {
      this.temp = {}
      // 设置父级目录和id
      this.temp.parentDirectoryName = data.directoryName
      this.temp.parentDirectoryId = data.directoryId
      this.dialogTitle = '添加'
      this.directoryDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 重命名子目录
    rename(data) {
      this.temp = Object.assign({}, data)
      this.dialogTitle = '修改'
      this.directoryDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    editDirectory() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          editDirectory(tempData).then(res => {
            if (res.code === CODE_OK) {
              // 重新加载目录树
              this.loadData()
              this.directoryDialogFormVisible = false
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    // 删除目录
    remove(node, data) {
      this.$msgbox({
        title: '确认删除？',
        message: '删除之后无法恢复哦...',
        showCancelButton: true,
        confirmButtonText: '确认',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 删除目录
            deleteByDirectoryId(data.directoryId).then(res => {
              if (res.code === CODE_OK) {
                this.$message.success(res.msg)
                // 重新加载当前节点目录树
                this.loadData()
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
    // 点击树节点
    nodeClick(data) {
      this.$emit('nodeClick', data)
    }
  }
}
</script>
<style lang="scss" scoped>
.dir-container {
  padding: 20px;
  border-bottom: 1px solid #eee;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
