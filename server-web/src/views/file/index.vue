<template>
  <div class="app-container">
    <el-container style="border: 1px solid #eee">
      <!-- 目录树 -->
      <el-aside width="300px" class="aside">
        <directory @nodeClick="nodeClick"/>
      </el-aside>
      <!-- 内容区 -->
      <el-container>
        <el-main>
          <file-table ref="file-table" :directory-id="directoryId" :directory-name="directoryName"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import Directory from './directory'
import FileTable from './file'
export default {
  components: {
    Directory,
    FileTable
  },
  data() {
    return {
      // 文件目录id
      directoryId: null,
      directoryName: ''
    }
  },
  methods: {
    // 点击树节点
    nodeClick(data) {
      this.directoryId = data.directoryId
      this.directoryName = data.directoryName
      this.$nextTick(() => {
        this.$refs['file-table'].handleFilter()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.aside{
  border: 1px solid #eee;
  background-color: #fff
}
</style>