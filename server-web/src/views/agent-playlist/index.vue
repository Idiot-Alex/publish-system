<template>
  <div class="app-container">
    <el-container style="border: 1px solid #eee">
      <el-aside width="300px" class="aside">
        <el-tree
          :data="treeList"
          :props="treeProps"
          lazy
          highlight-current
          node-key="agentId"
          @node-click="nodeClick">
          <span slot-scope="{ node, data }" class="custom-tree-node">
            <span>{{ node.label + ' [ ' + data.agentCode + ' ]'}}</span>
          </span>
        </el-tree>
      </el-aside>
      <!-- 内容区 -->
      <el-container>
        <el-main>
          <div ref="filter-container" class="filter-container">
            <el-button :disabled="Object(tempAgent).length === 0" class="filter-item" type="primary" icon="el-icon-s-promotion" @click="handlePublish">发布</el-button>
          </div>
          <!-- 播单列表 -->
          <draggable v-model="agentPlaylist" @change="onChange">
            <el-row v-for="item in agentPlaylist" :key="item.listId">
              <el-col :span="24">
                <el-card>
                  <span style="display: inline-block; width: 80%;">{{ item.title }}</span>
                  <el-button type="primary" size="mini" style="text-align: right;" @click="handleDelete(item)">删除</el-button>
                </el-card>
              </el-col>
            </el-row>
          </draggable>
        </el-main>
      </el-container>
    </el-container>
    <!-- 发布区 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="820px">
      <div class="app-container" v-if="dialogVisible">
        <!-- 搜索区 -->
        <div ref="filter-container" class="filter-container">
          <el-input v-model="listQuery.title" placeholder="标题" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
        </div>
        <!-- 文件列表 -->
        <el-row v-loading="listLoading" :gutter="5" class="file-wrap">
          <el-col v-for="item in articleList" :key="item.articleId" :span="8">
            <div class="file-col" @click="choose(item)">
              <el-image :src="item.coverImage" fit="scale-down" class="picture"/>
              <span>{{ item.title }}</span>
              <i v-show="tempArticle.articleId === item.articleId" class="el-icon-success" :class="{'file-active': tempArticle.articleId === tempArticle.articleId}"/>
            </div>
          </el-col>
        </el-row>
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
    </el-dialog>
  </div>
</template>
<script>
import { getUserAgents } from '@/api/agent'
import { getAgentPlaylistByAgentId, addAgentPlaylist, editAgentPlaylist, deleteByListId } from '@/api/agent-playlist'
import { getArticles } from '@/api/article'
import draggable from 'vuedraggable'
export default {
  components: {
    draggable
  },
  data() {
    return {
      // 树属性
      treeList: [],
      treeProps: {
        children: 'children',
        label: 'agentName'
      },
      agentPlaylist: [],
      dialogVisible: false,
      // 发布文稿
      listQuery: {
        pageNo: 1,
        pageSize: 20,
        editStatus: 1
      },
      articleList: [],
      totalCount: 0,
      listLoading: true,
      dialogTitle: '',
      tempAgent: {},
      tempArticle: {}
    }
  },
  created() {
    this.loadTreeData()
  },
  methods: {
    // 加载树结构
    loadTreeData() {
      const param = {
        pageNo: 1,
        pageSize: 65535
      }
      getUserAgents(param).then(res => {
        if (res.code === 0) {
          this.treeList = res.data
        }
      })
    },
    // 点击树节点
    nodeClick(data) {
      this.tempAgent = Object.assign({}, data)
      this.loadAgentPlaylist()
    },
    // 加载终端播单
    loadAgentPlaylist() {
      getAgentPlaylistByAgentId(this.tempAgent.agentId).then(res => {
        if (res.code === 0) {
          this.agentPlaylist = res.data
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
      this.loadArticles()
    },
    // 发布
    handlePublish() {
      this.handleFilter()
      this.dialogTitle = '发布文稿'
      this.dialogVisible = true
    },
    // 加载文稿
    loadArticles() {
      this.listLoading = true
      this.tempArticle = {}
      getArticles(this.listQuery).then(res => {
        // 加载数据成功
        if (res.code === 0) {
          this.totalCount = res.totalCount
          this.articleList = res.data
          this.listLoading = false
        } else {
          this.$notify.error({
            title: '失败',
            message: res.msg
          })
        }
      })
    },
    // 编辑
    onChange(event) {
      console.log(event)
      this.agentPlaylist.forEach((element, index) => {
        element.serialNumber = index
        editAgentPlaylist(element).then(res => {
          if (res.code !== 0) {
            this.$message.error('修改顺序失败')
          }
        })
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
            deleteByListId(row.listId).then(res => {
              if (res.code === 0) {
                this.$message.success(res.msg)
                // 更新
                this.loadAgentPlaylist()
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
    // 选择文稿
    choose(data) {
      this.tempArticle = Object.assign({}, data)
      // 添加
      const param = {
        agentId: this.tempAgent.agentId,
        articleId: this.tempArticle.articleId
      }
      addAgentPlaylist(param).then(res => {
        if (res.code === 0) {
          this.$message.success('发布成功')
          this.dialogVisible = false
          this.loadAgentPlaylist()
        } else {
          this.$message.error('发布失败')
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.aside{
  border: 1px solid #eee;
  background-color: #fff;
  height: calc(100vh - 100px);
}

.file-wrap {
  height: 400px;
  overflow-y: scroll;
  .file-col {
    height: 135px;
    background-color: #f3efef;
    margin-bottom: 5px;
    text-align: center;
    .picture {
      width: 200px;
      height: 100px;
      margin-top: 5px;
    }
    .video {
      width: 200px;
      height: 100px;
      margin-top: 5px;
    }
    span {
      margin: 0 5px;
      display: block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
.file-active {
  position: relative;
  display: inline;
  z-index: 99;
  font-size: 40px;
  color: #71c071;
  top: -90px;
}
</style>