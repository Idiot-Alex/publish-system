<template>
  <div class="app-container">
    <!-- 搜索区 -->
    <div ref="filter-container" class="filter-container">
      <el-input v-model="listQuery.oldName" placeholder="文件名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.fileType" placeholder="文件类型" clearable style="width: 200px;" class="filter-item" @change="handleFilter">
        <el-option label="图片" value="1"/>
        <el-option label="视频" value="2"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
    </div>
    <!-- 文件列表 -->
    <el-row v-loading="listLoading" :gutter="5" class="file-wrap">
      <el-col v-for="item in list" :key="item.fileId" :span="8">
        <div class="file-col" @click="choose(item)">
          <el-image v-if="item.fileType === 1" :src="item.filePath" fit="scale-down" class="picture"/>
          <video v-else-if="item.fileType === 2" :src="item.filePath" controls class="video"/>
          <span>{{ item.oldName }}</span>
          <i v-show="temp.fileId === item.fileId" class="el-icon-success" :class="{'file-active': temp.fileId === item.fileId}"/>
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
</template>
<script>
import { getFiles } from '@/api/file'
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
      temp: {}
    }
  },
  mounted() {
    this.handleFilter()
  },
  methods: {
    // 加载文件
    loadData() {
      this.listLoading = true
      getFiles(this.listQuery).then(res => {
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
    // 选择文件
    choose(file) {
      this.temp = Object.assign({}, file)
      this.$emit('chooseFile', file)
    }
  }
}
</script>
<style lang="scss">
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